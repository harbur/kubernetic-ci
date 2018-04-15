def call(body) {

  def docker = new io.harbur.cmds.Docker()
  def bitBucket = new io.harbur.cmds.BitBucket()

  def stages = new io.harbur.stages.Stages()
  def charts = new io.harbur.stages.Charts()
  def captain = new io.harbur.stages.Captain()
  def environments = new io.harbur.stages.Environments()

  node ("jenkins-jenkins-slave"){
    try{
      bitBucket.inProgress()
      docker.login("docker.k8s.harbur.io")

      stages.checkout()

      captain.job()
      charts.job()
      environments.job()

      bitBucket.successful()
    } catch (e){
      bitBucket.failed()
      throw e
    }
  }
}
