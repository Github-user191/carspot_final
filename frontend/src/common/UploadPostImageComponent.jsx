import React from 'react';


const UploadPostImageComponent = (props) => {


    return (

        <div className="add-images-container">

            <div className="progress">
                <div
                    className="progress-bar progress-bar-info progress-bar-striped"
                    role="progressbar"
                    aria-valuenow={props.progress}
                    aria-valuemin="0"
                    aria-valuemax="100"
                    style={{width: props.progress + "%"}}
                >
                    {props.progress}%
                </div>
            </div>
            <h2>{props.heading}</h2>

            <form className="add-images-form">
                <div className="form-group add-images-form-group">
                    <div className="upload-container mb-4">
                        <label for="file-input">
                            <img src={props.uploadImgLabel} className="image-upload-icon" alt="upload image icon"/>
                            <input type="file" id="file-input" className="file-input shadow-none choose-category"
                                   onChange={props.onPostImageChange}/>

                            <p className="upload-text">Ads with pictures receive more views and sell faster.</p>
                        </label>


                    </div>


                    {props.postImages && (

                        <div className="preview-images-container">
                            <div className="card-header">
                                List of Files
                            </div>

                            <ul className="list-group list-group-flush">
                                {props.postInfo &&

                                    props.postInfo.map((file, index) => (
                                        <li className="list-group-item" key={index}>
                                            <p>pog</p>
                                            <p>{file.url}</p>
                                            <img className="uploaded-image" src={file.url} alt={file.wname}/>
                                            <button className="btn btn-warning">Delete</button>
                                        </li>
                                    ))}

                            </ul>
                        </div>
                    )}


                </div>
            </form>
        </div>


    )
};


export default UploadPostImageComponent;
