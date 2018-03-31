def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p admin123"
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:04d0fc5")
        kc_image.inside('-v /home/jenkins/.docker/:/root/.docker/')  {
  
          stage ('Checkout') {
            checkout scm
          }
  
          stage ('Build') {
            sh "printenv"
            sh "pwd"
            sh "whoami"
            sh "ls -la /root"
            sh "kc build -t ${BRANCH_NAME}"
          }
  
          stage ('Push') {
            sh "kc push -t ${BRANCH_NAME}"
          }
        }
      }
    } catch (e){
      throw e
    }
  }
}
