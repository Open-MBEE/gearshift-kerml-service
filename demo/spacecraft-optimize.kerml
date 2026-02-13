// ============================================================
// Gearshift Demo: Trade Study — Minimize Total Mass
// ============================================================
// Same class-based rollup, but now the solver optimizes:
// "What is the lightest spacecraft that meets all requirements?"
// ============================================================

class Propulsion {
    feature mass : ScalarValues::Integer;
}

class Power {
    feature mass : ScalarValues::Integer;
}

class Avionics {
    feature mass : ScalarValues::Integer;
}

class Thermal {
    feature mass : ScalarValues::Integer;
}

class Structure {
    feature mass : ScalarValues::Integer;
}

class Spacecraft {
    feature totalMass : ScalarValues::Integer;
    feature payloadMass : ScalarValues::Integer;

    inv { totalMass == Propulsion::mass + Power::mass + Avionics::mass + Thermal::mass + Structure::mass + payloadMass }

    inv { Propulsion::mass >= 3000 }
    inv { Power::mass >= 500 }
    inv { Avionics::mass >= 200 }
    inv { Thermal::mass >= 100 }
    inv { Structure::mass >= 1500 }
    inv { payloadMass >= 1000 }
}
