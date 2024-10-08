package com.ia.smartkaufen.services;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BatchProcessorService {

    private final OllamaService ollamaService;
    private final JdbcTemplate jdbcTemplate;

    public void processShoppingList(int userID, double budget, int days, int people) {
        int offset = 0; // Comenzar desde el primer registro
        int batchSize = 100; // Tamaño del lote
        boolean moreData = true;

        // Obtener la lista de compras actual de la base de datos (si existe)
        String currentList = getCurrentListFromDatabase(userID);

        while (true) {
            // 1. Seleccionar el lote de productos
            List<Map<String, Object>> products = fetchProductBatch(offset, batchSize);

            if (products.isEmpty()) {
                moreData = false; // No hay más productos, salir del bucle
                break;
            }

            // 2. Formar la pregunta y los datos a enviar a la IA
            String requestData = buildRequestData(products, userID, budget, days, people, currentList);

            // 3. Enviar los datos a la IA y esperar la respuesta
            String responseData = ollamaService.sendDataToServer(requestData);
            BigDecimal totalPrice = calculateTotalAmount(responseData);

            // 4. Procesar la respuesta de la IA y actualizar la lista de compras
//            currentList = processAIResponse(responseData, userID, currentList);

            // 5. Guardar la lista actualizada en la base de datos
            saveShoppingListToDatabase(userID, responseData, totalPrice);

            // 6. Incrementar el offset para obtener el siguiente lote
            offset += batchSize;
        }

        // Finalizar el proceso
        finalizeShoppingList(userID, currentList);
    }

    private List<Map<String, Object>> fetchProductBatch(int offset, int batchSize) {
        String sql = """
        WITH ProductData AS (
            SELECT 
                P.ProductID,
                P.Name,
                P.Brand,
                P.CategoryID,
                P.Price,
                P.Stock,
                P.ExpirationDate,
                P.Organic,
                P.GlutenFree,
                P.Description,
                N.Energy,
                N.Protein,
                N.TotalFat,
                N.Carbohydrates,
                N.Fiber,
                N.Sugar,
                N.Sodium
            FROM 
                Product P
            LEFT JOIN
                Nutrition N ON P.ProductID = N.ProductID
        )
        SELECT * 
        FROM ProductData
        ORDER BY ProductID
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;
    """;
        return jdbcTemplate.queryForList(sql, offset, batchSize);
    }

    private String buildRequestData(List<Map<String, Object>> products, int userID, double budget, int days, int people, String currentList) {
        StringBuilder requestData = new StringBuilder();

        if (currentList.isEmpty()) {
            // Primera instrucción (crear lista)
            requestData.append(String.format("{ \"instruction\": \"Create an optimized shopping list based on a sophisticated algorithm. The goal is to maximize the value of the products included while staying within the given budget of %s euros. The list should return the following for each product: ProductID, Name, Price, and any other relevant attributes that contribute to the optimization. The algorithm should consider factors such as price-to-quality ratio, customer preferences, product availability, and budget constraints. The selection process should ensure the best balance between cost-effectiveness and product quality.\", \"products\": [", budget));
        }
        else {
            // Instrucción repetitiva (mejorar lista)
            requestData.append(String.format("{ \"instruction\": \"Optimize the current shopping list based on a sophisticated algorithm that maximizes product value while adhering to a budget constraint of %s euros. The algorithm should consider the following: (1) Maximize the utility of the items based on price-to-quality ratio, customer preferences, and product availability. (2) Ensure that the total price does not exceed the given budget. The optimization should also take into account the products in the current list and the new additional products, forming a super list that is more efficient and valuable for the user. The list should return ProductID, Name, Price, and any other relevant attributes that contribute to the optimization process. The final list should be the best possible within the provided constraints.\", \"currentList\":", budget))
                    .append(currentList).append(", \"products\": [");
        }


        for (Map<String, Object> product : products) {
            requestData.append("{")
                    .append("\"ProductID\":").append(product.get("ProductID")).append(",")
                    .append("\"Name\":\"").append(product.get("Name")).append("\",")
                    .append("\"Brand\":\"").append(product.get("Brand")).append("\",")
                    .append("\"Price\":").append(product.get("Price")).append(",")
                    .append("\"Organic\":").append(product.get("Organic")).append(",")
                    .append("\"GlutenFree\":").append(product.get("GlutenFree")).append(",")
                    .append("\"Energy\":").append(product.get("Energy")).append(",")
                    .append("\"Protein\":").append(product.get("Protein")).append(",")
                    .append("\"TotalFat\":").append(product.get("TotalFat")).append(",")
                    .append("\"Carbohydrates\":").append(product.get("Carbohydrates")).append(",")
                    .append("\"Fiber\":").append(product.get("Fiber")).append(",")
                    .append("\"Sugar\":").append(product.get("Sugar")).append(",")
                    .append("\"Sodium\":").append(product.get("Sodium")).append("},");

        }

        if (!products.isEmpty()) {
            requestData.setLength(requestData.length() - 1); // Quitar la última coma
        }

        requestData.append("], \"budget\":").append(budget)
                .append(", \"days\":").append(days)
                .append(", \"people\":").append(people)
                .append(", \"userID\":").append(userID)
                .append("}");

        return requestData.toString();
    }

    public String processAIResponse(String responseData, int userID, String currentList) {

        JSONObject responseJSON = new JSONObject(responseData);
        JSONArray currentListJSON = new JSONArray(currentList);

        JSONArray shoppingList = responseJSON.getJSONArray("shoppingList");

        for (Object product : shoppingList) {
            JSONObject productJSON = (JSONObject) product;
            currentListJSON.put(productJSON);
        }

        JSONObject finalList = new JSONObject();
        finalList.put("userID", userID);
        finalList.put("shoppingList", currentListJSON);

        return finalList.toString();
    }

    private void finalizeShoppingList(int userID, String finalList) {
        // Aquí se finaliza el proceso de la lista de compras (por ejemplo, guardándola en la BD)
        // Utiliza finalList para actualizar los datos de la lista de compras en la base de datos
    }

    private String getCurrentListFromDatabase(int userID) {
        String sql = "SELECT ShoppingListData FROM ShoppingList WHERE UserID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID}, String.class);
        } catch (Exception e) {
            return ""; // No hay lista existente
        }
    }

    private void saveShoppingListToDatabase(int userID, String shoppingListData, BigDecimal totalPrice) {
        String sql = """
    MERGE ShoppingList AS target
    USING (SELECT ? AS UserID, ? AS ShoppingListData, ? AS TotalAmount) AS source
    ON (target.UserID = source.UserID)
    WHEN MATCHED THEN
        UPDATE SET ShoppingListData = source.ShoppingListData, TotalAmount = source.TotalAmount
    WHEN NOT MATCHED THEN
        INSERT (UserID, ShoppingListData, TotalAmount) 
        VALUES (source.UserID, source.ShoppingListData, source.TotalAmount);
""";
        jdbcTemplate.update(sql, userID, shoppingListData, totalPrice);
    }

    private BigDecimal calculateTotalAmount(String shoppingListData) {
        JsonFactory factory = new JsonFactory();
        BigDecimal totalValue = BigDecimal.ZERO;

        try {
            JsonParser parser = factory.createParser(shoppingListData);

            while (!parser.isClosed()){
                JsonToken jsonToken = parser.nextToken();

                if (JsonToken.FIELD_NAME.equals(jsonToken) && parser.getCurrentName().equals("Price")) {
                    jsonToken = parser.nextToken();
                    totalValue = totalValue.add(parser.getDecimalValue());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error when parsing JSON: " + e.getMessage());
        }

        return totalValue;
    }
}
