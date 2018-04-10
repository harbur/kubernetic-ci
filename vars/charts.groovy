def call(body) {

  def dockerCmd = new io.harbur.DockerCmd()
  def gitCmd = new io.harbur.GitCmd()
  def helmCmd = new io.harbur.HelmCmd()
  def bitBucketCmd = new io.harbur.BitBucketCmd()

  node ("jenkins-jenkins-slave"){
    try{
      bitBucketCmd.inProgress()
      dockerCmd.login("docker.k8s.harbur.io")
      gitCmd.checkout()

      stage ('Build') {
        helmCmd.init()
        helmCmd.pack()
      }

      stage ('Test') {
        helmCmd.test()
      }

      stage ('Push') {
// def userInput = input(
//  id: 'userInput', message: 'Let\'s promote?', parameters: [
//  [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env']
// ])
// echo ("Env: "+userInput)

        sh '''
          for file in `ls -1 docs/*.tgz`; do
            curl  -F "chart=@$file" http://chartmuseum-chartmuseum:8080/api/charts
          done
        '''
      }
      bitBucketCmd.successful()
    } catch (e){
      bitBucketCmd.failed()
    }
  }
}
