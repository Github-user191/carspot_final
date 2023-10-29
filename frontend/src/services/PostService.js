import axios from "axios";
import { webStore } from '../utils/WebStore';

const BASE_URL = `${webStore.APP_URL}/api`;

class PostService {

    async createPost(images, post) {

        const formData = new FormData();
        const json = JSON.stringify(post);
        const blob = new Blob([json], {
            type: "application/json"
        });

        Object.keys(images).forEach(i => {
            // console.log("from PostService ", i, files[i]);
            // Appending all images added to post to formData
            formData.append("image", images[i])
        })

        // appending Post information to formData with proper name 
        formData.append("post", blob);
        
        // POST request with images and post information together
        return axios.post(BASE_URL + `/post/create`, formData, {headers:{'Content-Type':'multipart/form-data'}})
        
    }

    async updatePost(images, post, postId) {

        const formData = new FormData();
        const json = JSON.stringify(post);
        const blob = new Blob([json], {
            type: "application/json"
        });

        Object.keys(images).forEach(i => {
            formData.append("image", images[i])
        })

        // appending Post information to formData
        formData.append("post", blob);
        
        // POST request with images and post information together
        return axios.put(BASE_URL + `/post/update/${postId}`, formData, {headers:{'Content-Type':'multipart/form-data'}})
        
    }
    

    async getPostById(id) {
        return await axios.get(BASE_URL + `/post/${id}`)
    }

    async getAllPosts(params) {
        return await axios.get(BASE_URL + `/posts/search`, {params})
    }

    async getAllPostsCount() {
        return await axios.get(BASE_URL + `/posts/user/count`)
    }

    async getPostCountByBrand() {
        return await axios.get(BASE_URL + `/posts/brand/count`)
    }

    async getPostCountByProvince() {
        return await axios.get(BASE_URL + `/posts/province/count`)
    }

    async getPostViewsByPostId(id) {
        return await axios.get(BASE_URL + `/post/views/${id}`)
    }

    async getUserActivePosts(params) {
        return await axios.get(BASE_URL + `/posts/user/active`, {params})
    }

    async getAllUserPosts(params) {
        return await axios.get(BASE_URL + `/posts/user/all`, {params})
    }

    async getUserPostsByEmailAddress(emailAddress) {
        return await axios.get(BASE_URL + `/posts/user/active/` + emailAddress)
    }


    async deletePosts(id) {
        return await axios.delete(BASE_URL + `/posts/${id}`)
    }

    async deletePostImageById(id) {
        return await axios.delete(BASE_URL + `/post/image/${id}`)
    }

    async getPostImages() {
        return await axios.get(`${BASE_URL}/images`)
    }

    
}

export default new PostService();