import { HttpHeaders } from "@angular/common/http"

const BASE_URL = "http://localhost:8080/api/v1"
export const API_URL = {
    login: BASE_URL + "/auth/authenticate",
    getUser: BASE_URL + "/users/get",
    updateUser: BASE_URL + "/users/update",
    getUserEmail: BASE_URL + "/users/get-by-email",
    getAllTopic: BASE_URL + "/topic/find-all",
    getTopicOfProblem: BASE_URL + "/topic/get-topic-of-problem",
    addProblem: BASE_URL + "/problem/add",
    getAllProblem: BASE_URL + "/problem/get-all",
    getProblem: BASE_URL + "/problem/get",
    getProblemByCreator: BASE_URL + "/problem/get-by-creator",
    getProblemByKeyword: BASE_URL + "/problem/get-by-keyword",
    updateState: BASE_URL + "/problem/update-state",
    updateProblem: BASE_URL + "/problem/update",
    uploadFile: BASE_URL + "/file/upload-file",
    deleteFolder: BASE_URL + "/file/delete-folder",
    submit: BASE_URL + "/submission/submit",
    getSubmission: BASE_URL + "/submission/result",
    getAllSubmission: BASE_URL + "/submission/get-all",
    getSubmissionOfProblem: BASE_URL + "/submission/get-submission-of-problem",
    getSubmissionByProblem: BASE_URL + "/submission/get-by-problemId",
    addContest: BASE_URL + "/contest/add",
    getContest: BASE_URL + "/contest/get",
    updateContest: BASE_URL + "/contest/update",
    getChallenges: BASE_URL + "/contest/get-challenges",
    getParticipants: BASE_URL + "/contest/get-participants",
    addSubmissionContest: BASE_URL + "/contest/add-submission",
    getSubmissionContest: BASE_URL + "/contest/get-submission",
    getAllSubmissionOfContest: BASE_URL + "/contest/get-all-submission",
    getMySubmissionOfContest: BASE_URL + "/contest/get-my-submission",
    getSubmissionOfProblemInContest: BASE_URL + "/contest/get-submission-of-problem"
}

export const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', `Bearer ${sessionStorage.getItem('access_token')}`)




