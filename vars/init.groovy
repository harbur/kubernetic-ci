def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p admin123"
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:04d0fc5")
        kc_image.inside('-v /home/jenkins/.docker/:/root/.docker/ -v /usr/bin/docker:/usr/bin/docker')  {
  
          stage ('Checkout') {
            checkout scm
          }
  
          stage ('Build') {
            sh "printenv"
            sh "pwd"
            sh "whoami"
            sh "ls -la /usr/bin/docker"
            sh "ls -la /var/run/"
            sh "kc build -t ${BRANCH_NAME}"
          }

          stage('Input') {
def userInput = input(
 id: 'userInput', message: 'Let\'s promote?', parameters: [
 [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env'],
 [$class: 'TextParameterDefinition', defaultValue: 'uat1', description: 'Target', name: 'target']
])
echo ("Env: "+userInput['env'])
echo ("Target: "+userInput['target'])

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
