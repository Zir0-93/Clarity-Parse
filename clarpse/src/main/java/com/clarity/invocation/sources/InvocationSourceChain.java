package com.clarity.invocation.sources;

import java.util.List;

import com.clarity.invocation.ComponentInvocation;

public abstract class InvocationSourceChain {

    private final List<InvocationSource> invocationSources;
    private int currentSourceIndex = 0;

    public InvocationSourceChain(List<InvocationSource> invocationSources) {
        this.invocationSources = invocationSources;
        if (invocationSources.size() < 1) {
            throw new IllegalArgumentException();
        }
    }

    public void process() {
        if (currentSourceIndex < (invocationSources.size())) {
            try {
                final ComponentInvocation invocation = invocationSources.get(currentSourceIndex)
                        .createComponentInvocation();
                if (!invocation.empty()) {

                    currentSourceIndex++;
                    if (currentSourceIndex < (invocationSources.size())) {
                        updateDependantInvocationSource(invocation, invocationSources.get(currentSourceIndex));
                        process();
                    }
                } else {
                    prepareInvocationSource(invocationSources.get(currentSourceIndex));
                }
            } catch (final Exception e) {
                // ignore...
                e.printStackTrace();
            }
        }
    }

    abstract void prepareInvocationSource(InvocationSource invocationSource);

    abstract void updateDependantInvocationSource(ComponentInvocation createdInvocation,
            InvocationSource dependantSource);

}