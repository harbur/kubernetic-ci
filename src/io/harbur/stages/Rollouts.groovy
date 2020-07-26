package io.harbur.stages

import io.harbur.cmds.Kubectl
import io.harbur.properties.Project
import io.harbur.properties.Global

/**
 * Class for Rollouts stages.
 */
class Rollouts {

  /**
   * Runs the following stages:
   *
   * - "Rollout Resources": It performs all Rollouts.
   *
   * @param script Reference to script scope.
   * @param project Project properties
   * @param global Global properties
   */
  static def run(def script, Project project, Global global) {
    if (project.rollouts) {
      rollout(script, project, global)
    }
  }

  private static def rollout(def script, project, global) {
    script.stage ('Rollout Resources') {
      Kubectl.rollout(script, project.rollouts)
    }
  }
}