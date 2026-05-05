-- Add indexes for frequently queried columns
-- This speeds up findByState, findByCropType queries significantly

CREATE INDEX idx_farmers_state 
    ON farmers(state);

CREATE INDEX idx_farmers_crop_type 
    ON farmers(crop_type);

CREATE INDEX idx_farmers_land_acres 
    ON farmers(land_acres);
