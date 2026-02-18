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