package SpacecraftDesign {
    doc /* A KerML model of a spacecraft with class-based subsystem
         hierarchy, specialization, mass budget rollup via solver
         constraints, and mission behaviors. */

    // ─── Abstract Base Types ───────────────────────────────

    abstract class Component {
        feature mass : ScalarValues::Integer;
        feature powerDraw : ScalarValues::Integer;
    }

    abstract class Subsystem :> Component {
        feature isRedundant : ScalarValues::Boolean;
    }

    // ─── Subsystem Classes ─────────────────────────────────

    class Propulsion :> Subsystem {
        feature thrust : ScalarValues::Integer;
        feature specificImpulse : ScalarValues::Integer;
        feature fuelMass : ScalarValues::Integer;
    }

    class Power :> Subsystem {
        feature solarArea : ScalarValues::Integer;
        feature batteryCapacity : ScalarValues::Integer;
        feature powerOutput : ScalarValues::Integer;
    }

    class Avionics :> Subsystem {
        feature processorSpeed : ScalarValues::Integer;
        feature antennaGain : ScalarValues::Integer;
    }

    class Thermal :> Subsystem {
        feature radiatorArea : ScalarValues::Integer;
        feature maxDissipation : ScalarValues::Integer;
    }

    class Structure :> Component {
        feature length : ScalarValues::Integer;
        feature diameter : ScalarValues::Integer;
    }

    // ─── Spacecraft Assembly ───────────────────────────────

    class Spacecraft {
        feature totalMass : ScalarValues::Integer;
        feature payloadMass : ScalarValues::Integer;
    }

    // ─── Mass Budget (Rollup) ──────────────────────────────

    package MassBudget {

        inv { Spacecraft::totalMass == Propulsion::mass + Power::mass + Avionics::mass + Thermal::mass + Structure::mass + Spacecraft::payloadMass }

        inv { Propulsion::mass >= 3000 }
        inv { Propulsion::mass <= 12000 }

        inv { Power::mass >= 500 }
        inv { Power::mass <= 3000 }

        inv { Avionics::mass >= 200 }
        inv { Avionics::mass <= 1500 }

        inv { Thermal::mass >= 100 }
        inv { Thermal::mass <= 800 }

        inv { Structure::mass >= 1500 }
        inv { Structure::mass <= 5000 }

        inv { Spacecraft::payloadMass >= 500 }
        inv { Spacecraft::payloadMass <= 4000 }

        inv { Spacecraft::totalMass <= 25000 }
    }

    // ─── Mission Behaviors ─────────────────────────────────

    behavior LaunchSequence {
        step preflightChecks;
        step ignition;
        step liftoff;
        step maxQ;
        step stageSeparation;
        step orbitalInsertion;

        succession first preflightChecks then ignition;
        succession first ignition then liftoff;
        succession first liftoff then maxQ;
        succession first maxQ then stageSeparation;
        succession first stageSeparation then orbitalInsertion;
    }

    behavior OrbitalManeuver {
        step attitudeAlign;
        step engineBurn;
        step coastPhase;
        step orbitVerify;

        succession first attitudeAlign then engineBurn;
        succession first engineBurn then coastPhase;
        succession first coastPhase then orbitVerify;
    }

    behavior DeployPayload {
        step openBayDoors;
        step extendArm;
        step releasePayload;
        step retractArm;

        succession first openBayDoors then extendArm;
        succession first extendArm then releasePayload;
        succession first releasePayload then retractArm;
    }
}
