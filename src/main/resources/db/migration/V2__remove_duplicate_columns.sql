-- Remove duplicate columns from medicine_schedule table
ALTER TABLE medicine_schedule DROP COLUMN IF EXISTS medicine_name;
ALTER TABLE medicine_schedule DROP COLUMN IF EXISTS dosage; 