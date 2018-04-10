def call(body) {

  def dockerCmd = new io.harbur.DockerCmd()
  def gitCmd = new io.harbur.GitCmd()
  def helmCmd = new io.harbur.HelmCmd()

  node ("jenkins-jenkins-slave"){
    try{
      bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'build', buildName: 'Build')
      dockerCmd.login("docker.k8s.harbur.io")
      gitCmd.checkout()

      stage ('Build') {
        helmCmd.init()

        sh '''
          for i in `ls -1 charts`; do
            helm dep build "charts/$i"
            helm package --destination docs "charts/$i"
          done
        '''
      }
  
      stage ('Test') {
        sh "helm lint charts/*"
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
      bitbucketStatusNotify( buildState: 'SUCCESSFUL', buildKey: 'build', buildName: 'Build')
    } catch (e){
      bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'build', buildName: 'Build',
        buildDescription: 'Something went wrong with build!'
      )
    }
  }
}
