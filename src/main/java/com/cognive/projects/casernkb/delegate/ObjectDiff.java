package com.cognive.projects.casernkb.delegate;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.node.DiffNode;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashSet;
import java.util.Set;

public class ObjectDiff implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Object work = delegateExecution.getVariable("diffWork");
        Object base = delegateExecution.getVariable("diffBase");

        DiffNode node = ObjectDifferBuilder.buildDefault().compare(work, base);
        Set<String> paths = new HashSet<>();
        if(node.hasChanges()) {
            node.visit((diffNode, visit) -> {
                String path = filterPath(diffNode.getPath().toString());
                if(path != null)
                    paths.add(path);
            });
        }
        delegateExecution.setVariable("diffChanges", node.hasChanges());
        delegateExecution.setVariable("diffPaths", paths);
    }

    String filterPath(String path) {
        String result = path;
        if(result.startsWith("/"))
            result = result.substring(1);
        result = result.replace('/', '.');
        if(result.isEmpty())
            return null;

        return result;
    }
}
