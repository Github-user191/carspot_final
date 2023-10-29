// color: var(--dark-grey-foreground);
// border: none;
// background-color: var(--light-grey-background);

export const customStylesSort = {
    placeholder: (provided) => ({
        ...provided,
        color: '#ffffff',
    }),

    menu: (provided, state) => ({
      ...provided,

    }),
    option: (provided, state) => ({
      ...provided,
      // fontSize: "var(--paragraph-size-mobile)",
      
    }),
    indicatorSeparator: (styles) => (
      {display:'none'}
    ),
    dropdownIndicator: (provided) => ({
      ...provided,
      "svg": {
        fill: "#fff",
        height: "18px"
      }
    }),
    control: (provided, state) => ({
      ...provided,
      textAlign: "center",
      border: "none",
      boxShadow: 'none',
      padding: "5px",
      transition: ".3s",
      backgroundColor: "var(--primary-dark-blue)",
      fontSize: "var(--button-text-size-mobile)",
    
    }),
    singleValue: (provided, state) => ({
      ...provided,
      color: "#fff"
    })
  }

  
  
  export const customStylesContactSelect = {
    menu: (provided, state) => ({
      ...provided,
      color: state.selectProps.menuColor,
    }),
    option: (provided, state) => ({
      ...provided,
      // color: state.isSelected ? 'red' : 'blue',
    }),
    indicatorSeparator: (styles) => (
      {display:'none'}
    ),
    dropdownIndicator: (provided) => ({
      ...provided,
      "svg": {
        height: "18px",
        // marginTop: "-1rem"
      }
    }),
    control: (provided, state) => ({
      ...provided,
      boxShadow: 'none',
      border: "none",
      maxHeight: "100%"



    })
  }


  



  
  export const customStylesCreateAd = {
    menu: (provided, state) => ({
      ...provided,
      color: state.selectProps.menuColor,
      padding: 0
    }),
    option: (provided, state) => ({
      ...provided,
      // color: state.isSelected ? 'red' : 'blue',
    }),
    indicatorSeparator: (styles) => (
      {display:'none'}
    ),
    dropdownIndicator: (provided) => ({
      ...provided,
      "svg": {
        height: "18px"
      }
    }),
    control: (provided, state) => ({
      ...provided,
      // border: "none",
      width: "100%",
      boxShadow: 'none',
      fontSize: "var(--button-text-size-mobile)",
      padding: "2px",
      border: "none",
      height: "45px",
      
      backgroundColor: "var(--light-grey-background)"
    })
  }
