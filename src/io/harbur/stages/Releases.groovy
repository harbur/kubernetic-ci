package io.harbur.stages

import io.harbur.cmds.Helm
import io.harbur.properties.Project
import io.harbur.properties.Global

/**
 * Class for Releases stages.
 */
class Releases {

  /**
   * Runs the following stages:
   *
   * - "Upgrade Releases": It upgrades all Releases.
   *
   * @param script Reference to script scope.
   * @param project Project properties
   * @param global Global properties
   */
  static def run(def script, Project project, Global global) {
    if (project.releases) {
      upgrade(script, project, global)
    }
  }

  private static def upgrade(def script, project, global) {
    script.stage ('Upgrade Releases') {
      Helm.addRepos(script, global.repos)
      Helm.upgrade(script, project.releases)
    }
  }
}