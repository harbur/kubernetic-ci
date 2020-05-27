package io.harbur.stages

import io.harbur.cmds.Helm
import io.harbur.properties.Project
import io.harbur.properties.Global

/**
 * Class for Charts stages.
 */
class Charts {

  /**
   * Runs the following stages:
   *
   * - "Build Charts": It builds all Charts.
   * - "Test Charts": It tests all Charts with `helm lint`.
   * - "Push Charts": It pushes all Charts to the chartRepo.
   *
   * @param script Reference to script scope.
   * @param project Project properties
   * @param global Global properties
   */
  static def run(def script, Project project, Global global) {
    if (project.charts) {
      build(script, project, global)
      test(script, project, global)
      push(script, project, global)
    }
  }

  private static def build(def script, Project project, Global global) {
    script.stage ('Build Charts') {
      Helm.addRepos(script, global.repos)
      Helm.pack(script, project.charts)
    }
  }

  private static def test(def script, Project project, Global global) {
    script.stage ('Test Charts') {
      Helm.test(script, project.charts)
    }
  }

  private static def push(def script, Project project, Global global) {
    script.stage ('Push Charts') {
      Helm.push(script, global.chartRepo)
    }
  }
}