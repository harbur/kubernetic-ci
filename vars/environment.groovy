def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p sQe3tokiLm"
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:55688e4")
        kc_image.inside('-v /home/jenkins/.docker/:/root/.docker/ -v /usr/bin/docker:/usr/bin/docker')  {
  
          stage ('Checkout') {
            checkout scm
          }
  
          stage ('Deploy') {
            bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'deploy', buildName: 'Deploy')

            try {
              sh "helm upgrade -i sandbox env/sandbox/"

              bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'deploy', buildName: 'Deploy')
            } catch(Exception e) {
              bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'deploy', buildName: 'Deploy',
                buildDescription: 'Something went wrong with deploy!'
              )
            }  
          }
        }
      }
    } catch (e){
      throw e
    }
  }
}
