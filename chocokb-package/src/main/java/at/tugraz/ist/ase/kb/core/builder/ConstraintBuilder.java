/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.kb.core.builder;

import at.tugraz.ist.ase.common.ConstraintUtils;
import at.tugraz.ist.ase.fm.core.Relationship;
import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.NonNull;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.nary.cnf.LogOp;

/**
 * This builder is used by {@link at.tugraz.ist.ase.kb.camera.CameraKB},
 * {@link at.tugraz.ist.ase.kb.pc.PCKB}, and {@link at.tugraz.ist.ase.kb.renault.RenaultKB}
 */
public class ConstraintBuilder implements IConstraintBuildable {
    /**
     * Post a Choco constraint to the model and add generated Choco constraints to the {@link Constraint} object.
     * If hasNegativeConstraints is true, the function also post the negation of the Choco constraint.
     * @param constraintName The name of the constraint.
     * @param modelKB The model to post the constraint to.
     * @param chocoConstraint The Choco constraint to post.
     * @param startIdx The index of the first Choco constraint.
     * @param hasNegativeConstraints Whether the constraint has negative constraints.
     * @return The {@link Constraint} object.
     */
    @Override
    public Constraint buildConstraint(String constraintName, @NonNull Model modelKB,
                                      @NonNull org.chocosolver.solver.constraints.Constraint chocoConstraint,
                                      int startIdx, boolean hasNegativeConstraints) {
        modelKB.post(chocoConstraint);

        org.chocosolver.solver.constraints.Constraint negChocoConstraint = null;
        if (hasNegativeConstraints) {
            negChocoConstraint = chocoConstraint.getOpposite();
            modelKB.post(negChocoConstraint);
        }

        // in case of hasNegativeConstraints is true
        // after posting the negChocoConstraint
        // Ex: there are five constraints in the modelKB: c1, c2, c3, c4, c5
        // c1, c2, c3, c4 will be added to the list of chocoConstraints in the Constraint object
        // c1, c2, c3, c5 will be added to the list of negChocoConstraints in the Constraint object

        // create the Constraint object
        Constraint constraint = new Constraint(constraintName);
        ConstraintUtils.addChocoConstraintsToConstraint(constraint, modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraint.addChocoConstraints(modelKB, startIdx, modelKB.getNbCstrs() - 1, hasNegativeConstraints);
//        constraintList.add(constraint);

        // remove c5 from the modelKB
        // if we keep c5 in the modelKB, the model will be inconsistent
        if (hasNegativeConstraints && negChocoConstraint != null) {
            modelKB.unpost(negChocoConstraint);
        }

        return constraint;
    }

    @Override
    public Constraint buildConstraint(Relationship relationship, @NonNull Model modelKB, @NonNull LogOp logOp, LogOp negLogOp, int startIdx, boolean hasNegativeConstraints) {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}
