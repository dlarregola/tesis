package LogicaAplicacion;

import java.util.Comparator;

public class SortByDistancia implements Comparator<DistanciaSalida> {
    // Used for sorting in ascending order of
    // roll number
    public int compare(DistanciaSalida a, DistanciaSalida b)
    {
        return a.getDistancia().intValue() - b.getDistancia().intValue();
    }
}
