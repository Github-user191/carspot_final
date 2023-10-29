import React, { useEffect, useState } from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import uploadImgLabel from './assets/upload.svg';
import {AiOutlineWarning} from 'react-icons/ai'
import {GoCheck} from 'react-icons/go'
import {IoMdClose} from 'react-icons/io'
import FooterComponent from './common/FooterComponent';
import { Link } from 'react-router-dom';
import Select from 'react-select';
import axios from 'axios';
import customStyles, { customStylesCreateAd } from './common/CustomSelectStyles';
import PostService from './services/PostService';
import UploadPostImageComponent from './common/UploadPostImageComponent';
import { Bounce, Slide, toast, ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import postSuccessIllustration from './assets/post-success-illustration.svg'
import brands from "./utils/brand_model.json"
import provinces from "./utils/province_city.json"
import car1 from "./assets/audi.jpg"
import car2 from "./assets/toyota.jpg"
import AddOrUpdateImageSection from './common/AddOrUpdateImageSection';
import LoadingComponent from './common/LoadingComponent';
import PostSuccessModal from "./PostSuccessModal";

const CreateAdPage = () => {
      const [post, setPost] = useState({
        title:"",
        description:"",
        brand:"",
        model:"",
        price:"",
        color:"",
        kilometers:"",
        bodyType:"",
        fuelType:"",
        transmission:"",
        year:"",
        province:"",
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
        postAlreadyExistsError: "",
        fileUploadError: ""
        
    })

    const [loading ,setLoading] = useState(false);
    const [selectedImages, setSelectedImages] = useState([]);
    const [previewImages, setPreviewImages] = useState([]);
    const [postSuccess, setPostSuccess] = useState(false);

    const [selectedModelOptions, setSelectedModelOptions] = useState([
      {value: "", label: ""}
    ]);
    
    const [selectedCityOptions, setSelectedCityOptions] = useState([
      {value: "", label: ""}
    ]);

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
        { value: 'grey', label: 'Grey' },
	{ value: 'orange', label: 'Orange'},
	{ value: 'green', label: 'Green'},
	{ value: 'blue', label: 'Blue'},
	{ value: 'turqoise', label: 'Turqoise'},
	{ value: 'purple', label: 'Purple'},
	{ value: 'pink', label: 'Pink'},
	{ value: 'yellow', label: 'Yellow'},
	{ value: 'brown', label: 'Brown'},
    ];	
    
    const handleTextChange = (event) =>{
      const {name, value} = event.target;
        setPost(prev => ({
            ...prev, [name]:value
        }))
    }
    
    const handlePriceAndKilometersNumericChange = (event) => {
      const re = /^[0-9]{0,7}$/;
      const {name, value} = event.target;

      if (event.target.value === '' || re.test(event.target.value)) {
        setPost(prev => ({
          ...prev, [name]:value
      }))
      }
    }

    const handleYearNumericChange = (event) =>{

      const re = /^[0-9]{0,4}$/;
      const {name, value} = event.target;

      if (event.target.value === '' || re.test(event.target.value)) {
        setPost(prev => ({
          ...prev, [name]:value
      }))
      }

  }

  const handleProvinceAndBrandChange = (selectedOption, event) => {
    
    setPost(prev => ({
      ...prev, [event.name] : selectedOption.label
    }));
    const models = brands.find((brand) => brand.id === selectedOption.value).models;
    if (!models) {
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



    const handleSubmit = (event) => {
        event.preventDefault();
  
        setLoading(true);
        const newPost = {
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

        PostService.createPost(selectedImages, newPost)
        .then(res => {
            setPostSuccess(true);

            toast.success("Post created successfully!")
        }).catch(err => {
            setLoading(false);

            setPostSuccess(false)
   
            const {title, description, brand, model, price, color, kilometers, bodyType, fuelType, transmission, year, province, city, postAlreadyExists, fileUploadError} = err.response.data;
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
              cityError: city,
              postAlreadyExistsError: postAlreadyExists
            })


            if(postAlreadyExists) {
              toast.error(postAlreadyExists)
            }

            if(fileUploadError) {
              toast.warn(fileUploadError)
            }
            

        }).finally(() => {
          setLoading(false)
  
        })
    }    

    const removeImage = (id) => {
      setPreviewImages(previewImages.filter(item => item !== previewImages[id]));
      setSelectedImages(selectedImages.filter(item => item !== selectedImages[id]));
    }

    

    const selectFile = (event) => {
        const fileList = event.target.files;
        let previewImagesArr = [...previewImages];
        let selectedImagesArr = [...selectedImages];
        
        for (let i = 0; i < fileList.length; i++) {
          const file = fileList[i];
          previewImagesArr.push(URL.createObjectURL(file));
          selectedImagesArr.push(file);
        
        }
        
        if (selectedImagesArr.length > 7) {
            toast.warn("You cannot add more than 7 images per post")
            return;
        }
        setSelectedImages(selectedImagesArr)
        setPreviewImages(previewImagesArr);
    };


    return (
      <div className="create-ad-page">
        <MobileNavigationHeader
          heading="Create your Ad"
          infoDefault="Please be sure to provide detailed information when creating an ad and refer to the "
          infoColored="safety tips."
          link={"/safety-tips"}
        />
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
                    <Select name="brand" onChange={handleProvinceAndBrandChange} placeholder="Select brand" options={brandOptions} styles={customStylesCreateAd} />
  
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Model</label>
                    {post.model ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.modelError}</label>}
                    <Select name="model" isDisabled={post.brand === "" ? true : false} onChange={handleSelectChange} placeholder="Select model" options={selectedModelOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Transmission</label>
                    {post.transmission ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.transmissionError}</label>}
                    <Select name="transmission" onChange={handleSelectChange} placeholder="Select transmission" options={transmissionOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Body type</label>
                    {post.bodyType ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.bodyTypeError}</label>}
                    <Select name="bodyType" onChange={handleSelectChange} placeholder="Select brand" options={bodyTypeOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Fuel type</label>
                    {post.fuelType ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.fuelTypeError}</label>}
                    <Select name="fuelType" onChange={handleSelectChange} placeholder="Select brand" options={fuelTypeOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Color</label>
                    {post.color ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.colorError}</label>}
                    <Select name="color" onChange={handleSelectChange} placeholder="Select brand" options={colorOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Kilometers</label>
                    {post.kilometers ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.kilometersError}</label>}
                    <input type="text" placeholder="Kilometers" className="form-control shadow-none" name="kilometers" value={post.kilometers} onChange={handlePriceAndKilometersNumericChange} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Year</label>
                    {post.year ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.yearError}</label>}
                    <input type="text" placeholder="Year" className="form-control shadow-none" name="year" value={post.year} onChange={handleYearNumericChange} />
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
                    {post.description && post.description.length >= 50 && post.description.length <= 2000 ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.descriptionError}</label>}
                    <textarea placeholder="Description" className="form-control shadow-none" id="form-text-area" name="description" value={post.description} onChange={handleTextChange} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Price</label>
                    {post.price ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.priceError}</label>}
                    <input type="text" placeholder="Price" className="form-control shadow-none" name="price" value={post.price} onChange={handlePriceAndKilometersNumericChange} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">Province</label>
                    {post.province ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.provinceError}</label>}
                    <Select name="province" onChange={handleProvinceAndBrandChange} placeholder="Select province" options={provinceOptions} styles={customStylesCreateAd} />
                  </div>

                  <div className="mb-2">
                    <label className="form-label">City</label>
                    {post.city ? <GoCheck size={15} id="checkmark-success-status" /> : <label id="form-error-status">{errors.cityError}</label>}
                    <Select name="city" isDisabled={post.province === "" ? true : false} onChange={handleSelectChange} placeholder="Select city" options={selectedCityOptions} styles={customStylesCreateAd} />
                  </div>


                </div>
              </form>
            </div>


              <AddOrUpdateImageSection addOrUpdateText={'Add'} uploadImgLabel={uploadImgLabel} removeImage={removeImage} selectFile={selectFile} previewImages={previewImages}/>
          </div>


          <div className="post-ad-container mb-5">
            {/* <button className="post-ad-btn" data-bs-toggle="modal" data-bs-target={postSuccess ? "#postSuccessModal" : null} onClick={handleSubmit}>
              {loading ? <LoadingComponent color={"#fff"} /> : 'Post Ad'}
            </button> */}

            <button className="post-ad-btn"  onClick={handleSubmit}>
              {loading ? <LoadingComponent color={"#fff"} /> : 'Post Ad'}
            </button>

            <p>
              Please be advised of the
              <Link to="/safety-tips" style={{ textDecoration: "none" }}>
                <span style={{ color: "var(--primary-light-blue)", fontWeight: "600", }}>
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
    );
}

export default CreateAdPage
