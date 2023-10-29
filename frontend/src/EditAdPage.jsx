import React, {useEffect, useState} from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import {GoCheck} from 'react-icons/go'
import upload from './assets/upload.svg';
import {IoMdClose} from 'react-icons/io'
import FooterComponent from './common/FooterComponent';
import { Link,useParams} from 'react-router-dom'
import UserService from './services/UserService'
import Select from 'react-select';
import PostService from './services/PostService'
import brands from "./utils/brand_model.json"
import provinces from "./utils/province_city.json"
import { customStylesCreateAd } from './common/CustomSelectStyles';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import AddOrUpdateImageSection from './common/AddOrUpdateImageSection';


const EditAdPage = () => {

    // creating a new post
    const [post, setPost] = useState({
        id: "",
        title: "",
        description: "",
        brand: "",
        model: "",
        price: "",
        color: "",
        kilometers: "",
        bodyType: "",
        fuelType: "",
        transmission: "",
        year: "",
        province: "",
        city: ""
    })

    const [errors, setErrors] = useState({
        titleError:"",
        descriptionError:"",
        brandError:"",
        modelError:"",
        priceError:"",
        colorError:"",
        kilometersError:"",
        bodyTypeError:"",
        fuelTypeError:"",
        transmissionError:"",
        yearError:"",
        provinceError:"",
        cityError: "", 
        postAlreadyExistsError: ""
        
    })


    const brandOptions = brands.map(brand => ({
        "value": brand.id,
        "label":brand.brand
      }))
  
      const provinceOptions = provinces.map(location => ({
        "value": location.id,
        "label": location.province
      }))
  
      const cityOptions = provinces.filter(province => province.province).map(city => ({
        "value": city.cities.map(c => c.id),
        "label": city.cities.map(c => c.city),
      }))
  

    const transmissionOptions = [
        { value: 'automatic', label: 'Automatic' },
        { value: 'manual', label: 'Manual' }
    ];

    const bodyTypeOptions  = [
        { value: 'coupe', label: 'Coupe' },
        { value: 'hatchback', label: 'Hatchback' },
        { value: 'convertable', label: 'Convertable' },
        { value: 'sedan', label: 'Sedan' },
        { value: 'wagon', label: 'Wagon' },
        { value: 'suv', label: 'SUV' }
    ];

    const fuelTypeOptions = [
        { value: 'petrol', label: 'Petrol' },
        { value: 'diesel', label: 'Diesel' },
        { value: 'electric', label: 'Electric' },
        { value: 'hybrid', label: 'Hybrid' }
    ];

    const colorOptions = [
        { value: 'red', label: 'Red' },
        { value: 'white', label: 'White' },
        { value: 'black', label: 'Black' },
        { value: 'grey', label: 'Grey' }
    ];


    const [selectedModelOptions, setSelectedModelOptions] = useState([
        {value: "", label: ""}
      ]);
  
      const [selectedCityOptions, setSelectedCityOptions] = useState([
        {value: "", label: ""}
      ]);

    // the chosen image/s
    const [totalImageCount, setTotalImageCount] = useState(0);
    const [selectedImages, setSelectedImages] = useState([]);
    // display the chosen image/s
    const [previewImages, setPreviewImages] = useState([]);
    //const [postImagesFileId, setPostImagesFileId] = useState([]);
    const [postImages, setPostImages] = useState([]);


    const { id } = useParams();

    const selectFile = (event) => {
        const fileList = event.target.files;
        // setSelectedImage(fileList);
    

        let previewImagesArr = [...previewImages];
        let selectedImagesArr = [...selectedImages];
        
        for (let i = 0; i < fileList.length; i++) {
          const file = fileList[i];
          previewImagesArr.push(URL.createObjectURL(file));
          selectedImagesArr.push(file);


        }


        if(selectedImagesArr.length + totalImageCount > 7) {
            toast.warn("You cannot add more than 7 images per post")
            return;
        }

        setSelectedImages(selectedImagesArr)
        setPreviewImages(previewImagesArr);
      };

      

    useEffect(() => {
        PostService.getPostById(id)
            .then(res => {

                const data = res.data;
                setPost(data)
                setTotalImageCount(data.postImages.length)
                setPostImages(data.postImages)

                let tempModelList = brands.filter(m =>m.brand.includes(res.data.brand))[0].models
                const modelsSelect = tempModelList.map(({id,model}) => ({ value: id, label: model })) 
                setSelectedModelOptions(modelsSelect)

            }).catch(err => {

            })
    }, [])

    const handleTextChange = (event) => {
        const { name, value } = event.target;
        setPost(prev => ({
            ...prev, [name]: value
        }))

    }

    const handleSubmit = (event) => {
        event.preventDefault();

        const newPost = {
            id: id,
            title: post.title,
            description: post.description,
            brand: post.brand,
            model: post.model,
            price: post.price,
            color: post.color,
            kilometers: post.kilometers,
            bodyType: post.bodyType,
            fuelType: post.fuelType,
            transmission: post.transmission,
            year: post.year,
            province: post.province,
            city: post.city
        }
        
    
        PostService.updatePost(selectedImages, newPost, id)
        .then(res => {
            toast.success("Post was updated successfully!")
        }).catch(err => {
            const {title, description, brand, model, price, color, kilometers, bodyType, fuelType, transmission, year, province, city} = err.response.data;
            setErrors({
              titleError: title,
              descriptionError: description,
              brandError: brand,
              modelError: model,
              priceError: price,
              colorError: color,
              kilometersError: kilometers,
              bodyTypeError: bodyType,
              fuelTypeError: fuelType,
              transmissionError:transmission,
              yearError: year,
              provinceError: province,
              cityError: city
            })

        })

    }

    const removeImage = (id) => {
        if(id.length !== 36) {  
              setPreviewImages(previewImages.filter(item => item !== previewImages[id]));
              setSelectedImages(selectedImages.filter(item => item !== selectedImages[id]));
      
        } else {
            PostService.deletePostImageById(id)
        .then(res => {
            setPostImages(postImages.filter(image => image.id !== id))
        }).catch(err => {
        })
        }
        

    }

    const handleProvinceAndBrandChange = (selectedOption, event) => {
    
        setPost(prev => ({
          ...prev, [event.name] : selectedOption.label
        }));
        const models = brands.find((brand) => brand.id === selectedOption.value).models;
        if (!models) {
          // handle error
          return;
        }
    
        const modelsSelect = models.map(({id,model}) => ({ value: id, label: model })) 
    
        setSelectedModelOptions(modelsSelect);
    
    
        const cities = provinces.find((province) => province.id === selectedOption.value).cities;
    
        if (!cities) {
          return
        }
        const citiesSelect = cities.map(({ id, city }) => ({ value: id, label: city }))
        setSelectedCityOptions(citiesSelect);
      }


    const handleSelectChange = (selectedOption, event) => {

          setPost(prev => ({
            ...prev, [event.name] : selectedOption.label
          }));
  
      };

    return (
        <div className="edit-ad-page">
            <MobileNavigationHeader heading={"Edit your ad"} infoDefault={"Change some information regarding your ad."} />
            <div className="content">
                <div className="row ">
                    <div className="vehicle-information-container col-md">

                        <h2>Vehicle Information</h2>

                        <form className="vehicle-information-form">
                            <div className="form-group vehicle-information-form-group">
                                <div className="mb-2">
                                    <label className="form-label">Brand</label>
                                    {/* <AiOutlineWarning size={15} id="warning-status" /> */}
                                    {post.brand ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.brandError}</label>}
                                    <Select name="brand" value={{label : post.brand}} onChange={handleProvinceAndBrandChange} placeholder="Select brand" options={brandOptions} styles={customStylesCreateAd} />
                             
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Model</label>
                                    {post.model ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.modelError}</label>}
                                    <Select name="model" value={{label : post.model}} isDisabled={post.brand === "" ? true : false} onChange={handleSelectChange} placeholder="Select model" options={selectedModelOptions} styles={customStylesCreateAd} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Transmission</label>
                                    {post.transmission ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.transmissionError}</label>}
                                    <Select name="transmission" value={{label: post.transmission}} onChange={handleSelectChange} placeholder="Select transmission" options={transmissionOptions} styles={customStylesCreateAd} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Body type</label>
                                    {post.bodyType ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.bodyTypeError}</label>}
                                    <Select name="bodyType" value={{label : post.bodyType}} onChange={handleSelectChange} placeholder="Select brand" options={bodyTypeOptions} styles={customStylesCreateAd} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Fuel type</label>
                                    {post.fuelType ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.fuelTypeError}</label>}
                                    <Select name="fuelType" value={{label : post.fuelType}} onChange={handleSelectChange} placeholder="Select brand" options={fuelTypeOptions} styles={customStylesCreateAd} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Color</label>
                                    {post.color ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.colorError}</label>}
                                    <Select name="color" value={{label : post.color}} onChange={handleSelectChange} placeholder="Select brand" options={colorOptions} styles={customStylesCreateAd}/>
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Kilometers</label>
                                    {post.kilometers ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.kilometersError}</label>}
                                    <input type="text" placeholder="Kilometers" className="form-control shadow-none" name="kilometers" value={post.kilometers} onChange={handleTextChange} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Year</label>
                                    {post.year ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.yearError}</label>}
                                    <input type="number" placeholder="Year" className="form-control shadow-none" name="year" value={post.year} onChange={handleTextChange}/>
                                </div>
                            </div>
                        </form>
                    </div>

                    <div className="ad-information-container col-md">
                        <h2>Ad Information</h2>

                        <form className="ad-information-form">
                            <div className="form-group ad-information-form-group">
                                <div className="mb-2">
                                    <label className="form-label">Ad title</label>
                                    {post.title && post.title.length >= 20 && post.title.length <= 100? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.titleError}</label>}
                                    <input type="text" placeholder="Ad title" className="form-control shadow-none" name="title" value={post.title} onChange={handleTextChange} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Description</label>
                                    {post.description && post.description.length >= 50 ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.descriptionError}</label>}
                                    <textarea placeholder="Description" className="form-control shadow-none" id="form-text-area" name="description" value={post.description} onChange={handleTextChange} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Price</label>
                                    {post.price ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.priceError}</label>}
                                    <input type="text" placeholder="Price" className="form-control shadow-none" name="price" value={post.price} onChange={handleTextChange} />
                                </div>

                                <div className="mb-2">
                                    <label className="form-label">Province</label>
                                    {post.province ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.provinceError}</label>}
                                    <Select name="province" value={{label : post.province}} onChange={(e) => handleSelectChange("province", e)} placeholder="Select province" options={provinceOptions} styles={customStylesCreateAd} />
                                </div>
                            
                                <div className="mb-2">
                                    <label className="form-label">City</label>
                                    {post.city ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.cityError}</label>}
                                    <Select name="city" value={{label : post.city}} onChange={(e) => handleSelectChange("city", e)} placeholder="Select city" options={selectedCityOptions} styles={customStylesCreateAd} />
                                </div>



                            </div>
                        </form>
                    </div>
                </div>

    
                {/* {selectedImages} */}

                <AddOrUpdateImageSection addOrUpdateText={'Update'} uploadImgLabel={upload} removeImage={removeImage} selectFile={selectFile} postImages={postImages} previewImages={previewImages}/>

                <div className="edit-ad-container mb-5">
                    <button className="update-ad-btn" onClick={handleSubmit}>
                        Update Ad
                    </button>
                    <p>
                        Please be advised of the
                        <Link to="/safety-tips" style={{ textDecoration: "none" }}>
                            <span style={{color: "var(--primary-light-blue)", fontWeight: "600",}}>
                                {" "} safety tips{" "}
                            </span>
                        </Link>
                        to prevent being scammed.
                    </p>
                </div>


            </div>

            <div id="footer-component">
                <FooterComponent />
            </div>

        </div>
    )
}

export default EditAdPage
