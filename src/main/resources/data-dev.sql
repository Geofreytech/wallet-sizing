-- Insert Dummy Corporate Customers
INSERT INTO customers (cif, name, kra_pin, industry, segment) VALUES
                                                                  ('CIF1001', 'Green Horizon Tea Ltd', 'P051234567A', 'Agriculture', 'LARGE_ENTERPRISE'),
                                                                  ('CIF1002', 'Nairobi Tech Hub Sacco', 'P059876543B', 'Financial Services', 'SACCO'),
                                                                  ('CIF1003', 'Mombasa Logistics Group', 'P051122334C', 'Transport', 'SME');

-- Insert Dummy Internal Financial Data (As per SDD 5.2.2)
INSERT INTO financial_data (customer_id, category, amount, currency) VALUES
                                                                         (1, 'LOAN_BALANCE', 5000000.00, 'KES'),
                                                                         (1, 'DEPOSIT_BALANCE', 12000000.00, 'KES'),
                                                                         (2, 'DEPOSIT_BALANCE', 45000000.00, 'KES');


-- Section 5.2.6: Reference Data - Banks
CREATE TABLE IF NOT EXISTS banks (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     bank_name VARCHAR(255) NOT NULL,
    bank_code VARCHAR(20) UNIQUE
    );

INSERT IGNORE INTO banks (bank_name, bank_code) VALUES
('I&M Bank', 'IMBL'),
('KCB Bank', 'KCB'),
('Equity Bank', 'EQTY');

-- Add an initial Wallet Sizing record for Green Horizon Tea Ltd
-- Assuming customer_id 1 is Green Horizon
INSERT IGNORE INTO wallet_sizings
(customer_id, financial_period, annual_turnover, total_market_debt, internal_debt, wallet_share_percentage, status, created_by, created_at)
VALUES
(1, 'FY2025', 50000000.00, 20000000.00, 5000000.00, 25.00, 'DRAFT', 'geoffrey.kimani', NOW());