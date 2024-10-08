
CREATE TABLE ProductCategory (
                                 CategoryID INT PRIMARY KEY IDENTITY(1,1),
                                 Name VARCHAR(100) NOT NULL,
                                 Description VARCHAR(255),
                                 UNIQUE(Name) -- A unique index ensures no duplicate category names
);

CREATE TABLE Product (
                         ProductID INT PRIMARY KEY IDENTITY(1,1),
                         Name VARCHAR(255) NOT NULL,
                         Brand VARCHAR(100) NOT NULL,
                         CategoryID INT FOREIGN KEY REFERENCES ProductCategory(CategoryID),
                         Price DECIMAL(10, 2) NOT NULL,
                         Stock INT NOT NULL,
                         ExpirationDate DATE NULL,
                         Weight DECIMAL(10, 3) NULL,
                         Volume DECIMAL(10, 3) NULL,
                         UPC VARCHAR(15) UNIQUE NOT NULL, -- Barcode
                         Description VARCHAR(255),
                         Allergens VARCHAR(255),
                         Organic BIT DEFAULT 0, -- 0 = No, 1 = Yes
                         GlutenFree BIT DEFAULT 0, -- 0 = No, 1 = Yes
                         DateAdded DATETIME DEFAULT GETDATE(),
                         LastUpdated DATETIME DEFAULT GETDATE(),
                         INDEX idx_ProductName (Name), -- Index on product name for faster searches
                         INDEX idx_ProductBrand (Brand), -- Index on brand for filtering by brand
                         INDEX idx_ProductPrice (Price), -- Index on price for range queries
                         INDEX idx_CategoryID (CategoryID) -- Foreign key index for category queries
);

CREATE TABLE Nutrition (
                           ProductID INT PRIMARY KEY FOREIGN KEY REFERENCES Product(ProductID),
                           Energy DECIMAL(10,2),
                           Protein DECIMAL(10,2),
                           TotalFat DECIMAL(10,2),
                           Carbohydrates DECIMAL(10,2),
                           Fiber DECIMAL(10,2),
                           Sugar DECIMAL(10,2),
                           Sodium DECIMAL(10,2)
);

CREATE TABLE Store (
                       StoreID INT PRIMARY KEY IDENTITY(1,1),
                       Name VARCHAR(100),
                       Location VARCHAR(255),
                       OpeningTime TIME,
                       ClosingTime TIME,
                       INDEX idx_StoreName (Name), -- Index on store name
                       INDEX idx_StoreLocation (Location) -- Index on store location
);

CREATE TABLE StoreProduct (
                              ProductID INT FOREIGN KEY REFERENCES Product(ProductID),
                              StoreID INT FOREIGN KEY REFERENCES Store(StoreID),
                              Price DECIMAL(10, 2) NOT NULL,
                              Stock INT NOT NULL,
                              PRIMARY KEY (ProductID, StoreID),
                              INDEX idx_ProductStore (ProductID, StoreID), -- Composite index on ProductID and StoreID for faster lookups
                              INDEX idx_StoreID (StoreID), -- Index on store ID for store-specific queries
                              INDEX idx_StorePrice (Price) -- Index on price for ordering/filtering by price
);

CREATE TABLE Promotion (
                           PromotionID INT PRIMARY KEY IDENTITY(1,1),
                           ProductID INT FOREIGN KEY REFERENCES Product(ProductID),
                           DiscountPercentage DECIMAL(5, 2),
                           StartDate DATE,
                           EndDate DATE,
                           Description VARCHAR(255),
                           INDEX idx_PromotionProduct (ProductID), -- Index on ProductID for product promotions
                           INDEX idx_PromotionStartDate (StartDate) -- Index on StartDate for finding active promotions
);

CREATE TABLE ShoppingList (
                              ListID INT PRIMARY KEY IDENTITY(1,1),
                              UserID INT NOT NULL,
                              ShoppingListData NVARCHAR(MAX),
                              DateCreated DATETIME DEFAULT GETDATE(),
                              TotalAmount DECIMAL(10, 2) NOT NULL,
                              INDEX idx_UserID (UserID) -- Index on UserID for user-specific shopping list queries
);

CREATE TABLE ShoppingListProduct (
                                     ListID INT FOREIGN KEY REFERENCES ShoppingList(ListID),
                                     ProductID INT FOREIGN KEY REFERENCES Product(ProductID),
                                     Quantity INT NOT NULL,
                                     Subtotal DECIMAL(10, 2) NOT NULL,
                                     PRIMARY KEY (ListID, ProductID),
                                     INDEX idx_ProductList (ProductID, ListID) -- Composite index for faster product lookups in lists
);

CREATE TABLE UserPreferences (
                                 UserID INT PRIMARY KEY,
                                 PrefersOrganic BIT DEFAULT 0,
                                 GlutenFree BIT DEFAULT 0,
                                 LactoseFree BIT DEFAULT 0,
                                 Vegan BIT DEFAULT 0,
                                 INDEX idx_Organic (PrefersOrganic), -- Index on Organic preference
                                 INDEX idx_GlutenFree (GlutenFree), -- Index on GlutenFree preference
                                 INDEX idx_Vegan (Vegan) -- Index on Vegan preference
);

