package io.github.scitia.aleatoricism.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aleatoricism")
public class AleatoricismProperties {

    private final AutoRouterProperties autoRouter = new AutoRouterProperties();
    private final FlowProperties flow = new FlowProperties();

    public AutoRouterProperties getAutoRouter() {
        return autoRouter;
    }

    public FlowProperties getFlow() {
        return flow;
    }

    public static class AutoRouterProperties {

        private boolean enabled = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    public static class FlowProperties {

        private boolean waitForSideEffects = true;

        public boolean isWaitForSideEffects() {
            return waitForSideEffects;
        }

        public void setWaitForSideEffects(boolean waitForSideEffects) {
            this.waitForSideEffects = waitForSideEffects;
        }
    }
}

