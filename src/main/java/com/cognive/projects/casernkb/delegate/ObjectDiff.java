package com.cognive.projects.casernkb.delegate;

import de.danielbechler.diff.ObjectDifferBuilder;
import de.danielbechler.diff.circular.CircularReferenceMatchingMode;
import de.danielbechler.diff.node.DiffNode;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ObjectDiff implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        Object work = delegateExecution.getVariable("diffWork");
        Object base = delegateExecution.getVariable("diffBase");
        Object ignorePaths = delegateExecution.getVariable("ignorePaths");

        Set<String> ignoreSet = (ignorePaths != null) ?
                new HashSet<>((List)ignorePaths) :
                Collections.emptySet();

        DiffNode node = ObjectDifferBuilder.startBuilding().circularReferenceHandling()
                .matchCircularReferencesUsing(CircularReferenceMatchingMode.EQUALS_METHOD).and().build().compare(work, base);
        Set<String> paths = new HashSet<>();
        if(node.hasChanges()) {
            node.visit((diffNode, visit) -> {
                String path = filterPath(diffNode.getPath().toString());
                if(path != null && !ignoreSet.contains(path))
                    paths.add(path);
            });
        }

        delegateExecution.setVariable("diffChanges", !paths.isEmpty());
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
