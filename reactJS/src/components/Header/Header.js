import { Component } from "react";
import "./Header.css";

class Header extends Component {
  render() {
    return (
      <div className="header">
        <div className="inner-header">
          <img
            src="/assets/images/system/web_logo.png"
            className="logo"
            alt="Logo"
            width="250"
            height="65"
          />
        </div>
      </div>
    );
  }
}

export default Header;
