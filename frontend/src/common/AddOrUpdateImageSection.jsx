import React from 'react'
import {IoMdClose} from 'react-icons/io'
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";

const AddOrUpdateImageSection = ({addOrUpdateText, previewImages = [],postImages = [], uploadImgLabel, selectFile, removeImage}) => {
  return (
               <div id="add-or-update-images-container">
              <h2>{addOrUpdateText} photo(s)</h2>
              <span className='text-muted'>Please keep file size under 1Mb per file</span>

              {/* <AddImageSection /> */}

              <form className="add-or-update-images-form">
                <div className="form-group add-or-update-images-form-group">
                    <div className="upload-images-container mb-5">
  
                            <label for="file-input">
                                <img src={uploadImgLabel} className="image-upload-icon" alt="upload image icon"/>
                                <input type="file" id="file-input" multiple className="file-input shadow-none choose-category" onChange={selectFile} />
                                <p className="upload-text"> Ads with images receive more views and sell faster.</p>
                            </label>
         
                    </div>

                    <div className="row">
                                {previewImages.map((img, index) =>
                                    
                                    <div className="upload-image-list col">

                                        
                                        <div className='preview-images-container'>
                                            <img src={previewImages[index]} id={index} name={index} alt={"Uploaded image"} />
                                        </div>
                         
                                        <div className='btn-container'>
                                            <button type="button" onClick={() =>removeImage(index)} className="remove-img-btn">
                                                <IoMdClose size={18} className="remove-uploaded-image-icon" /> <span className='mr-3'>Remove image</span>
                                            </button>
                                        </div>

                                    </div>
                                )}

                                {postImages.map((img, index) =>
                                    <div className="upload-image-list col">
    
                                        <div className='preview-images-container'>
                                            <img src={img.imageUrl} alt={"Uploaded image"} />
                                        </div>

                                        <div className='btn-container'>
                                            <button type="button" className="remove-img-btn" onClick={() => removeImage(img.id)}>
                                                <IoMdClose size={18} className="remove-uploaded-image-icon" /> <span className="mr-3">Remove image</span>
                                            </button>
                                        </div>

                                    </div>
                                )}

                                <ToastContainer />

                            </div>



                        </div>
                    </form>
            </div>
  
  );
}

export default AddOrUpdateImageSection