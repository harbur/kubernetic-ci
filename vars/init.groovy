def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:04d0fc5")
        kc_image.inside  {
  
          stage ('Checkout') {
            checkout scm
          }
  
          stage ('Build') {
            sh "printenv"
            sh "kc build -t ${BRANCH_NAME}"
          }
  
          stage ('Push') {
            sh "docker login docker.k8s.harbur.io -u admin -p admin123"
            sh "kc push -t ${BRANCH_NAME}"
          }
        }
      }
    } catch (e){
      throw e
    }
  }
}
