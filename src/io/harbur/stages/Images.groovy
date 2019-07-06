package io.harbur.stages

import io.harbur.cmds.Docker
import io.harbur.properties.Project
import io.harbur.properties.Global

/**
 * Class for Images stages.
 */
class Images {

  /**
   * Runs the following stages:
   *
   * - "Build Images": It builds all Docker Images.
   *
   * @param script Reference to script scope.
   * @param project Project properties
   * @param global Global properties
   */
  static def run(def script, Project project, Global global) {
    if (project.images) {
      build(script, project, global)
    }
  }

  private static def build(def script, Project project, Global global) {
    script.stage ('Build Images') {
      Docker.build(script.docker, script, project.images, global.registry)
    }
  }
}