import React, { Component } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

// Page components
import Header from "../components/Header/Header";
import Footer from "../components/Footer/Footer";
// Pages
import Home from "./Home/Home";
// CSS
import "../util/Global.css";

class App extends Component {
  constructor(props) {
    super(props);

    this.state = {
      isLoaded: false,
      systems: [],
      getSystems: "http://localhost:8080/systems",
    };
  }

  componentDidMount() {
    fetch(this.state.getSystems)
      .then((res) => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            systems: result,
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
  }

  render() {
    const { error, isLoaded, systems } = this.state;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <div>Loading...</div>;
    } else {
      return (
        <React.Fragment>
          <Router>
            <Header />
            <Routes>
              <Route path="*" element={<Home systems={systems} />} />
            </Routes>
            <Footer />
          </Router>
        </React.Fragment>
      );
    }
  }
}

export default App;
