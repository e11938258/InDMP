import { Component } from "react";
import Text from "../Text/Text";
import "./Footer.css";

class Footer extends Component {
  render() {
    return (
      <footer>
        <Text value="Demonstration application for DMP service integration using maDMP | Filip Zoubek" />
      </footer>
    );
  }
}

export default Footer;
