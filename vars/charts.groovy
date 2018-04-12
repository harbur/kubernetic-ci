def call(body) {

  def docker = new io.harbur.cmds.Docker()
  def git = new io.harbur.cmds.Git()
  def helm = new io.harbur.cmds.Helm()
  def bitBucket = new io.harbur.cmds.BitBucket()

  node ("jenkins-jenkins-slave"){
    try{
      bitBucket.inProgress()
      docker.login("docker.k8s.harbur.io")
      git.checkout()

      stage ('Build') {
        helm.init()
        helm.pack()
      }

      stage ('Test') {
        helm.test()
      }

      stage ('Push') {
        helm.push()
      }

      bitBucket.successful()
    } catch (e){
      bitBucket.failed()
      throw e
    }
  }
}
