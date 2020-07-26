package io.harbur.cmds

import io.harbur.properties.Rollout

/**
 * Class for Kubectl commands.
 */
class Kubectl {

  /**
   * Performs rollout of resources.
   *
   * @param script Reference to script scope to access `sh()`
   * @param rollouts Rollouts defined in project `kubernetic.yaml`
   */
  static def rollout(def script, List<Rollout> rollouts) {
    for (rollout in rollouts) {
    script.sh(
      script: """
                kubectl rollout restart ${rollout.resource} -n ${rollout.namespace}
              """,
      label: "Rollout resource: ${rollout.resource} at namespace ${rollout.namespace}")
  }
}