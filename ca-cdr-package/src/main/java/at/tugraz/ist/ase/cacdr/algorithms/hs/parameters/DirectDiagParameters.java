/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hs.parameters;

import at.tugraz.ist.ase.kb.core.Constraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
public class DirectDiagParameters extends AbstractHSParameters {
    private Set<Constraint> B;

    @Builder
    public DirectDiagParameters(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        super(C);
        this.B = B;
    }

    @Override
    public String toString() {
        return "DirectDiagParameters{" +
                "C=" + getC() +
                ", B=" + B +
                "}";
    }

    @Override
    public void dispose() {
        super.dispose();
        B = null;
    }
}