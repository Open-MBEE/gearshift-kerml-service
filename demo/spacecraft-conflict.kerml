// ============================================================
// Gearshift Demo: Requirement Conflict Detection
// ============================================================
// The solver identifies contradictory requirements:
// the payload mass alone exceeds the total mass cap.
// ============================================================

class Propulsion {
    feature mass : ScalarValues::Integer;
}

class Spacecraft {
    feature totalMass : ScalarValues::Integer;
    feature payloadMass : ScalarValues::Integer;

    // Rollup (simplified)
    inv { totalMass == Propulsion::mass + payloadMass }

    // Requirement A: Heavy payload
    inv { payloadMass >= 10000 }

    // Requirement B: Lightweight vehicle
    inv { totalMass <= 5000 }

    // Requirement C: Propulsion has positive mass
    inv { Propulsion::mass >= 500 }
}
