import React from 'react'
import MobileNavigationHeader from './MobileNavigationHeader'
import avatar from './assets/avatar.svg'

const Chat = () => {
    return (
        <div className="chat-page">
            <MobileNavigationHeader heading={"Sami Jhan"} />
            <hr id="main-divider" style={{marginTop: "-1rem"}}/>
            <div className="content">
                <div className="chat-container">
                    <img src={avatar} alt="Buyer avatar"/>
                    

                    <div className="speech-bubble-buyer">
                        <p>Hello is this still available?</p>
                    </div>

                    <div className="speech-bubble-buyer">
                        <p>Please reply</p>
                    </div>

                    <div className="speech-bubble-buyer">
                        <p>Hello??</p>
                    </div>

                    <div className="speech-bubble-user">
                        <p>Lorem ipsum dolor, sit amet consectetur adipisicing elit. Voluptas obcaecati numquam est deleniti veritatis quis quos doloremque eveniet, delectus, nesciunt minus accusantium nemo nostrum sequi reprehenderit consequatur doloribus! Dolorem rerum placeat laudantium eos provident facere illum sunt at iure rem!</p>
                    </div>

                    <div className="speech-bubble-user">
                        <p>and yes it is</p>
                    </div>

                    
                </div>

                <div className="message-send-container">
                    <form className="message-form">
                        <div className="form-group">
                
                            <input type="text" placeholder="Type your message" className="form-control shadow-none" />

                            <button className="send-message-btn">Send</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    )
}

export default Chat