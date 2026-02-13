// ============================================================
// Gearshift Demo: Spacecraft Mass Budget — Solve
// ============================================================
// Subsystem mass allocations roll up to totalMass.
// The Z3 solver finds a valid allocation satisfying all
// bounds and the rollup equation.
// ============================================================

package MassBudget {
    feature totalMass : ScalarValues::Integer;
    feature propulsionMass : ScalarValues::Integer;
    feature powerMass : ScalarValues::Integer;
    feature avionicsMass : ScalarValues::Integer;
    feature thermalMass : ScalarValues::Integer;
    feature structureMass : ScalarValues::Integer;
    feature payloadMass : ScalarValues::Integer;

    // Rollup equation
    inv { totalMass == (propulsionMass + powerMass + avionicsMass + thermalMass + structureMass + payloadMass) }

    // Subsystem bounds (kg)
    inv { propulsionMass >= 3000 }
    inv { propulsionMass <= 12000 }

    inv { powerMass >= 500 }
    inv { powerMass <= 3000 }

    inv { avionicsMass >= 200 }
    inv { avionicsMass <= 1500 }

    inv { thermalMass >= 100 }
    inv { thermalMass <= 800 }

    inv { structureMass >= 1500 }
    inv { structureMass <= 5000 }

    inv { payloadMass >= 500 }
    inv { payloadMass <= 4000 }

    // System-level cap
    inv { totalMass <= 25000 }
}
