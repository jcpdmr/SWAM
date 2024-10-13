package com.swam.gateway.endToEnd;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.oristool.eulero.evaluation.approximator.TruncatedExponentialMixtureApproximation;
import org.oristool.eulero.evaluation.heuristics.AnalysisHeuristicsVisitor;
import org.oristool.eulero.evaluation.heuristics.RBFHeuristicsVisitor;
import org.oristool.eulero.modeling.Activity;
import org.springframework.test.context.ActiveProfiles;

import com.qesm.workflow.AbstractProduct;
import com.qesm.workflow.AbstractWorkflow;
import com.qesm.workflow.ProductInstance;
import com.qesm.workflow.ProductTemplate;
import com.qesm.tree.StructuredTree;
import com.qesm.tree.StructuredTreeConverter;
import com.qesm.workflow.WorkflowInstance;
import com.qesm.workflow.WorkflowTemplate;
import com.swam.commons.utility.DefaultWorkflows;

@ActiveProfiles("test")
public class AnalysisEndpointTest extends BaseEndpointTest {

    public AnalysisEndpointTest() {
        super("http://localhost:8080/api");
    }

    @Test
    public void AnalyzeTemplate() {
        // Test analysis
        analyze("/analysis/catalog/test", WorkflowTemplate.class, ProductTemplate.class);
    }

    @Test
    public void AnalyzeInstance() {
        // Instance template
        String newWorkflowInstanceId = null;

        String response = client.get().uri("/workflow/tobeinstantiated/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .returnResult()
                .getResponseBody();

        newWorkflowInstanceId = response.split(": ")[1];

        checkIfResponseIsEqualsToEntity("/workflow/operation/" + newWorkflowInstanceId,
                DefaultWorkflows.getWorkflowTemplateEntity2());

        // Test analysis
        analyze("/analysis/operation/" + newWorkflowInstanceId, WorkflowInstance.class, ProductInstance.class);

        // Delete instantiated workflow
        client.delete().uri("/workflow/operation/" + newWorkflowInstanceId)
                .exchange()
                .expectStatus().isOk();

    }

    private <W extends AbstractWorkflow<P>, P extends AbstractProduct> void analyze(String uri, Class<W> workflowClazz,
            Class<P> productClazz) {
        Map<Double, Double> analysisResult = uncheckedCast(client.get().uri(uri)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Map.class)
                .returnResult()
                .getResponseBody());

        // System.out.println(analysisResult);

        double[] cdfRecived = analysisResult.values().stream().mapToDouble(Double::doubleValue).toArray();

        W workflowTemplate = null;

        try {
            workflowTemplate = uncheckedCast(DefaultWorkflows.getWorkflowTemplateEntity2().convertAndValidate());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(workflowTemplate);

        StructuredTree<P> structuredTree = new StructuredTree<>(workflowTemplate.cloneDag(),
                productClazz);
        structuredTree.buildStructuredTree();
        StructuredTreeConverter converter = new StructuredTreeConverter(structuredTree.getStructuredWorkflow());
        Activity activity = converter.convertToActivity();
        AnalysisHeuristicsVisitor visitor = new RBFHeuristicsVisitor(BigInteger.valueOf(4), BigInteger.TEN,
                new TruncatedExponentialMixtureApproximation());
        double[] cdf = activity.analyze(activity.max().add(BigDecimal.ONE), activity.getFairTimeTick(), visitor);

        // for (int i = 0; i < cdf.length; i++) {
        // System.out.println("recived: " + cdfRecived[i] + " calculated: " + cdf[i]);
        // }

        assertArrayEquals(cdfRecived, cdf);
    }
}
