def call(body) {

  def docker = new io.harbur.cmds.Docker()
  def git = new io.harbur.cmds.Git()
  def helm = new io.harbur.cmds.Helm()
  def bitBucket = new io.harbur.cmds.BitBucket()
  def stages = new io.harbur.stages.Stages()
  def charts = new io.harbur.stages.Charts()

  node ("jenkins-jenkins-slave"){
    try{
      bitBucket.inProgress()
      docker.login("docker.k8s.harbur.io")

      stages.checkout()
      charts.job()

      bitBucket.successful()
    } catch (e){
      bitBucket.failed()
      throw e
    }
  }
}
