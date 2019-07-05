def call(body) {

  def docker = new io.harbur.cmds.Docker()
  def bitBucket = new io.harbur.cmds.BitBucket()

  def stages = new io.harbur.stages.Stages()

  node ("slave"){
    try{
      bitBucket.inProgress()

      stages.checkout()
      stages.jobs()

      bitBucket.successful()
    } catch (e){
      bitBucket.failed()
      throw e
    }
  }
}
