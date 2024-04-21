import axios from "axios";
import { webStore } from '../utils/WebStore';

const BASE_URL = `/api`;
class WatchlistPostService {

    async addWatchlistPost(postId) {
        return await axios.get(BASE_URL + `/post/watchlist/${postId}`)
    }

    async deleteWatchlistPosts(postId) {
        return await axios.delete(BASE_URL + `/post/watchlist/${postId}`)
    }

    async getUserWatchlistPosts(params) {
        return await axios.get(BASE_URL + `/posts/user/watchlist`, {params})
    }

}



export default new WatchlistPostService()