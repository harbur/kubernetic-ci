import io.harbur.utils.Stages

def call(body) {
  node ("slave"){
    try{
      Stages.jobs(this)
    } catch (e){
      throw e
    }
  }
}
