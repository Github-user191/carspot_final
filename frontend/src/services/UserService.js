import axios from "axios";
import { webStore } from '../utils/WebStore';


const BASE_URL = `${webStore.APP_URL}/api/user`;
const CONTACT_FORM_URL=`${webStore.APP_URL}/api/contact/send`;
const USER_REVIEW_URL=`${webStore.APP_URL}/api/review`;
class UserService {

    async getUserInfo() {
        return await axios.get(BASE_URL + "/info");
    }

    async getUserInfoByEmailAddress(emailAddress) {
        return await axios.get(BASE_URL + "/info/" + emailAddress);
    }

    async sendContactUsEmail(email) {
        return await axios.post(CONTACT_FORM_URL, email);
    }

    async updateUserInfo(user, avatar) {
        const formData = new FormData();
        const json = JSON.stringify(user);
        const blob = new Blob([json], {
            type: "application/json"
        });

        formData.append("avatar", avatar)

        // appending Post information to formData
        formData.append("user", blob);
        
        return await axios.post(BASE_URL + "/update", formData, {headers:{'Content-Type':'multipart/form-data'}});
    }

    async createUserReview(review) {
        return await axios.post(USER_REVIEW_URL + "/create", review);
    }

    async getAllUserReviews(params) {
        return await axios.get(USER_REVIEW_URL + "/all", {params});
    }

    async getUserReview() {
        return await axios.get(USER_REVIEW_URL + "/user/all");
    }

    async deleteReview(id) {
        return await axios.delete(USER_REVIEW_URL + `/${id}`);
    }
}

export default new UserService();