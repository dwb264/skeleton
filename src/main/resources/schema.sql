CREATE TABLE receipts (
  id INT UNSIGNED AUTO_INCREMENT,
  uploaded DATE DEFAULT CURRENT_DATE,
  merchant VARCHAR(255),
  amount DECIMAL(12,2),
  receipt_type INT UNSIGNED,

  PRIMARY KEY (id)
);

CREATE TABLE tags (
  tag VARCHAR(255),
  receiptId INT
);