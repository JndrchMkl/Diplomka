package messeges2.graph;

import org.knowm.xchart.internal.chartpart.Chart;

public interface ExampleChart<C extends Chart<?, ?>> {

    C getChart();

    String getExampleChartName();
}