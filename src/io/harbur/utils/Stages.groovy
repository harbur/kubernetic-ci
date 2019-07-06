package io.harbur.utils

import io.harbur.stages.Checkout
import io.harbur.properties.Project
import io.harbur.properties.Global
import io.harbur.stages.Images
import io.harbur.stages.Charts
import io.harbur.stages.Releases

/**
 * Class for running Stages.
 */
class Stages {

  /**
   * Bootstraps all Stages.
   *
   * @param script Reference to script scope
   */
  static def jobs(def script) {
    // Checkout and read Properties
    Checkout.run(script)
    Project project = Project.load(script)
    Global global = Global.load(script)

    // Bootstrap Stages
    Images.run(script, project, global)
    Charts.run(script, project, global)
    Releases.run(script, project, global)
  }
}