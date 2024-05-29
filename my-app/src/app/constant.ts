import { HttpHeaders } from "@angular/common/http"

const BASE_URL = "http://localhost:8080/api/v1"
export const API_URL = {
    login: BASE_URL + "/auth/authenticate",
    getUser: BASE_URL + "/users/get",
    getUserById: BASE_URL + "/users/get-by-id",
    updateUser: BASE_URL + "/users/update",
    getUserEmail: BASE_URL + "/users/get-by-email",
    getAllTopic: BASE_URL + "/topic/find-all",
    getTopicOfProblem: BASE_URL + "/topic/get-topic-of-problem",
    addProblem: BASE_URL + "/problem/add",
    getAllProblem: BASE_URL + "/problem/get-all",
    getProblem: BASE_URL + "/problem/get",
    getTotalRecordProblem: BASE_URL + "/problem/get-total-document",
    getProblemByCreator: BASE_URL + "/problem/get-by-creator",
    getProblemByKeyword: BASE_URL + "/problem/get-by-keyword",
    getSearchProblem: BASE_URL + "/problem/search",
    updateState: BASE_URL + "/problem/update-state",
    updateProblem: BASE_URL + "/problem/update",
    uploadFile: BASE_URL + "/file/upload-file",
    deleteFolder: BASE_URL + "/file/delete-folder",
    submit: BASE_URL + "/submission/submit",
    getSubmission: BASE_URL + "/submission/result",
    countTotalSubmissions: BASE_URL + "/submission/count-total-submissions",
    getAllSubmission: BASE_URL + "/submission/get-all",
    countSubmissionProblem: BASE_URL + "/submission/count-submissions-problem",
    getSubmissionOfProblem: BASE_URL + "/submission/get-submission-of-problem",
    getSubmissionByProblem: BASE_URL + "/submission/get-by-problemId",
    getStatistic: BASE_URL + "/submission/get-statistic",
    addContest: BASE_URL + "/contest/add",
    getContest: BASE_URL + "/contest/get",
    updateContest: BASE_URL + "/contest/update",
    getChallenges: BASE_URL + "/contest/get-challenges",
    getParticipants: BASE_URL + "/contest/get-participants",
    getSignups: BASE_URL + "/contest/get-signups",
    addSubmissionContest: BASE_URL + "/contest/add-submission",
    getSubmissionContest: BASE_URL + "/contest/get-submission",
    getAllSubmissionOfContest: BASE_URL + "/contest/get-all-submission",
    getMySubmissionOfContest: BASE_URL + "/contest/get-my-submission",
    getSubmissionOfProblemInContest: BASE_URL + "/contest/get-submission-of-problem",
    getLeaderBoardList: BASE_URL + "/contest/get-leader-board",
    getDetailLeaderboard: BASE_URL + "/contest/get-detail-leaderboard",
    getContestList: BASE_URL + "/contest/get-contest-list"
}

export const headers = new HttpHeaders()
    .set('Content-Type', 'application/json')
    .set('Authorization', `Bearer ${sessionStorage.getItem('access_token')}`)




