/* Copyright 2017,2020 Oracle and/or its affiliates. All rights reserved.
 * Version: 2020.12.06
 * Legal Notices: http://docs.oracle.com/cd/E23003_01/html/en/cpyr.htm
 */

(function() {
  /*
   * Initial code here ensures that we do not load global.js for sp-common-based publications
   * Note that return only exits the processing of this JavaScript module. The functions
   * before and after it still execute
   */
  if (window.ohcglobal) return;
  var s = document.querySelector("script[data-main]");
  if (s && s.getAttribute("data-main").indexOf("sp_common") >= 0) return;

  window.ohcglobal = "1.0.1";

  //For IE without debug console open
  if (!window.console) {
    window.console = {
      log: function() {}
    };
  }

  //Request to update/create robots meta element, compartible with ie
  var robotsElement = document.querySelector("head meta[name='robots']");
  var contentValue = robotsElement ? robotsElement.getAttribute("content").toLowerCase() : "";
  if (!robotsElement || contentValue === "all" || (contentValue.indexOf("index") !== -1 && contentValue.indexOf("follow") !== -1)) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/apps/ohcsearchclient/api/v1/search/robots?url=" + 
                    encodeURIComponent(window.location.origin + window.location.pathname.substring(0, window.location.pathname.lastIndexOf( "/" ) + 1)));
    xhr.send();
    xhr.onload = function () {
      if (xhr.status === 200 && xhr.responseText !== "all") {
        if (robotsElement) {
          document.querySelector("head meta[name='robots']").setAttribute("content", xhr.responseText);
        } else {
          var newRobotsElement = document.createElement("meta");
          newRobotsElement.name = "robots";
          newRobotsElement.content = xhr.responseText;
          document.querySelector("head").appendChild(newRobotsElement);
        }
      }
    }
  } 

  //Central log function so I can enable/disable debug messages
  var debug = true;
  //Turn debug off for production. Keep outside of function so we can override while debugging in prod
  if (window.location.hostname == "docs.oracle.com") debug = false;
  function log(msg) {
    if (debug) console.log(msg);
  }

  //Test for toc.htm[l] so we can know if we are dealing with a table of contents file
  function isToc() {
    if (window.location.href.indexOf("toc.htm") !== -1) {
      return true;
    } else {
      return false;
    }
  }

  //Test for http[s] so we can know if we are using other protocols like file://
  function isHttp() {
    //return true;
    if (window.location.href.split('/')[0].indexOf("http") === 0) {
      return true;
    } else {
      return false;
    }
  }

  //Is current window not in a frame?
  function isNotFramed() {
    try {
      //If the current window (self) is the same as the top window, then it is the main window
      return window.self === window.top;
    } catch (e) {
      return true;
    }
  }

  /* 
   * Currently only checking for frameset frames because regular hm strategy takes care of other types
   * We are returning the last frame of largest area, but it isn't the "main" frame that doesn't
   * have -frame in its name. We only need special frame management because the main framset window
   * doesn't work when you place the hm strategy footer on the main content window. We may have to
   * somehow discern between framesets that are used in different ways
   * 
   * Case in point: 10/15/2019: Added support for nested framesets 1 level deep using recursion
   */
  function checkFramesetMainFrame(parentFrame, level) {
    //Set default values for top-level window
    var frameset = window.top.document.getElementsByTagName('frameset');
    var frames = window.top.frames;

    //If we passed in a parentFrame element, then let's check for nested frameset
    if (parentFrame) {
      frameset = parentFrame.document.getElementsByTagName('frameset');
      frames = parentFrame.frames;
    }

    //The default is no frames. We check here to see if the content tells us otherwise (frameset only)
    if (frameset.length > 0) {
      var savedArea = 0;
      var frame = null;
      //Find the LAST largest frame (by area)
      for (var i = 0; i < frames.length; i++) {
        try {
          var area = frames[i].innerWidth * frames[i].innerHeight;
          if (area >= savedArea) {
            savedArea = area;
            frame = frames[i];
          }
        } catch (error) {
          log("checkMainFrame(" + level + ") Skipping frame (" + frames[i] + ": i=" + i + ") because it might be CORS, or experienced other error: " + error);
        }
      } //Looping through frames

      var nestedDoc = null;
      //Check for nested frameset (Caution... recursion 1 level deep)
      if (level == 0) {
        nestedDoc = checkFramesetMainFrame(frame, level + 1);
      }

      log("Returning the main content frameset window. Level=" + level + ((level > 0) ? " NESTED" : ""));
      if (nestedDoc) {
        //Nested main frame
        return nestedDoc;
      } else {
        //Main frame);
        return frame.document;
      }
    } //if in frameset

    log("Using main window / not a frameset");
    return null;
  }

  /* 
   * Strategy 1 is used to place the footer at the bottom of the page framed and not framed for
   * pages that we have determined we can reliably do so
   * 
   * Strategy 2 (the fixed, floating banner) is used to place the footer at the bottom of the browser window,
   * no frames to cover all pages as much as possible when we have no way of identifying them
   */
  function addFooterBannerHMStrategy() {
    var strategy1 = false;
    var msg = '<ul><li><div id="teconsent"></div></li><li><a id="adchoices" class="new-window" target="_blank" href="https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12">Ad Choices</a></li></ul>';

    //null param means use top-level for frames, 0 means top-level of recursion
    var doc = checkFramesetMainFrame(null, 0);
    if (!doc) {
      //If not a frame, then work on main document
      doc = document;

      //Check certain strategy 1 candidates
      if (doc.querySelector("a[href*='//www.bea.com/contact/index']")) {
        strategy1 = true;
        log('BEA');
      }

      if (document.querySelector('#copyright a[href*="/docs/legal/copyright.html"]')) {
        strategy1 = true;
        log('JavaSE docs/legal/copyright.html');
      }

      if (document.querySelector('a[href*="spec-frontmatter.html"]') ||
        document.querySelector('a[href*="jvms-0-front.html"]')) {
        strategy1 = true;
        log('JavaSE specs');
      }
      //If no strategy 1 candidates were detected, then we are doing strategy 2
    } else {
      strategy1 = true;
    }
    log("Using hm strategy " + ((strategy1 == true) ? "1" : "2"));

    //Check to see if HM strategy footer is already present and bow out if it does
    if (doc.querySelector("#footer-banner")) {
      log("#footer-banner already exists. Skipping application");
      return;
    }

    var rules = "";
    rules += "#footer-banner ul li:nth-child(2) {";
    rules += "  margin-left: 0;";
    rules += "}";

    rules += "#footer-banner {";
    rules += "  z-index: 9999;";
    if (strategy1) {
      //Strategy 1
      //Calculate banner width for HM strategy
      var width = doc.body.clientWidth; //Width without considering padding
      width = width - 24; //Remove our padding
      log("banner width=" + width);
      rules += "  position: relative;";
      rules += "  margin: 10 auto 0 auto;";
      rules += "  width: " + width + "px;";
    } else {
      //Strategy 2
      rules += "  position: fixed;";
      rules += "  margin: 0 auto 0 auto;";
    }
    rules += "  bottom: 0; left: 0; right: 0;";
    rules += "  background-color: #EBEBEB !important;";
    rules += "  border-top: 1px solid #ACB7BF;";

    rules += "  color: #1958AA;";
    rules += "  padding: 0.0em 1em;";
    rules += "  text-align: center;";
    rules += "  font-size: 12px !important;";
    rules += "  line-height: 15px;";
    rules += "  font-weight: normal;";
    rules += "  font-family: Arial;";
    rules += "  clear: both;";
    rules += "}";

    rules += "#teconsent {";
    rules += "  display: inline-block;"; //Just in case because it gets autogenerated otherwise unpredictably
    rules += "}";

    rules += "#footer-banner ul {";
    rules += "  list-style: none;";
    rules += "  font-size: 12px !important;";
    rules += "  padding: 0;";
    rules += "  margin: 0;";
    rules += "  margin-right: 20px;";
    rules += "}";

    rules += "#footer-banner li {";
    rules += "  display: inline-block;"; //Ensures single line
    if (strategy1) {
      rules += "  margin-top: 0;";
      rules += "  margin-bottom: 0;";
    }
    rules += "}";

    rules += "#footer-banner li + li:before {";
    rules += "  content: \"|\";";
    rules += "  margin-right: 4px;";
    rules += "  margin-left: 4px;";
    rules += "  color: #aaa;";
    rules += "}";

    rules += "#footer-banner a {";
    rules += "  color: #1958AA;";
    rules += "  text-decoration: none;";
    rules += "  font-size: 12px !important;";
    rules += "}";

    rules += "#footer-banner a:focus, #footer-banner a:hover {";
    rules += "  color: #1958AA;";
    rules += "  text-decoration: underline;";
    rules += "}";

    var myElem = document.getElementById('banner_footer_style');
    if (myElem === null) {
      addCss(doc, rules, "banner_footer_style");
    }

    myElem = document.getElementById('footer-banner');
    if (myElem === null) {
      modifyDocFooter(doc, msg);
    }
  
    window.top.disclaimerSet = true;
  }

  //Add CSS to target main content window only
  function addCss(doc, rules, id) {
    if (!doc) { doc = document; }
    var style = doc.createElement('style');
    style.setAttribute("id", id);
    style.type = 'text/css';
    if (style.styleSheet) {
      style.styleSheet.cssText = rules;
    } else {
      style.appendChild(document.createTextNode(rules));
    }
    doc.getElementsByTagName("head")[0].appendChild(style);
  }

  //This is where we actually attach our footer to the page
  function modifyDocFooter(doc, msg) {
    if (!doc) { doc = document; }
    var footerDiv = doc.createElement('div');
    footerDiv.setAttribute("id", "footer-banner");
    footerDiv.style.display = "inline-block";
    footerDiv.innerHTML = msg;
    doc.body.appendChild(footerDiv);
    applyConsent(doc);
  }

  //Places the cookie preference in place
  function applyConsent(doc) {
    if (!doc) { doc = document; }
    var consentScript = doc.createElement('script');
    consentScript.type = "text/javascript";
    consentScript.src = "https://consent.truste.com/notice?domain=oracle.com&c=teconsent&js=bb&noticeType=bb&text=true&gtm=1&cdn=1&pcookie";
    doc.body.appendChild(consentScript);
  }

  //This is where we attach our site catalyst to the page
  function addSiteCatalyst() {
    log("global.js site catalyst load");
    var oraScript = document.createElement('script');
    oraScript.type = "text/javascript";
    oraScript.src = "https://www.oracleimg.com/us/assets/metrics/ora_docs.js";
    document.getElementsByTagName('head')[0].appendChild(oraScript);
  }

  function isDoNotTrackSet() {
    if (navigator.doNotTrack == 1 || window.doNotTrack == 1 || navigator.msDoNotTrack == 1) {
      return true;
    } else {
      return false;
    }
  }

  /*
   * Default truste setting and script kept global so oracle reference is good for multiple functions
   */
  var trustechk = { "truste_consent": -1, "dnt": false };

  try {
    oracle.truste.api.getGdprConsentDecision();
  } catch (err) {
    var oracle = oracle || {};
    oracle.truste = {}, oracle.truste.api = {},
      function() { this.getCookieName = function() { return "notice_preferences" }, this.getStorageItemName = function() { return "truste.eu.cookie.notice_preferences" }, this.getGdprCookieName = function() { return "notice_gdpr_prefs" }, this.getGdprStorageItemName = function() { return "truste.eu.cookie.notice_gdpr_prefs" } }.apply(oracle.truste),
      function() {
        var e = oracle.truste;

        function t(e) {
          for (var t = e + "=", r = document.cookie.split(";"), n = 0; n < r.length; n++) {
            for (var o = r[n];
              " " == o.charAt(0);) o = o.substring(1);
            if (0 == o.indexOf(t)) return o.substring(t.length, o.length)
          }
          return null
        }

        function r(e) { var t, r = (t = e, "undefined" != typeof Storage ? localStorage.getItem(t) : null); return null != r ? JSON.parse(r).value : null }
        this.getConsentCode = function() { var n = r(e.getStorageItemName()) || t(e.getCookieName()); return null == n ? -1 : parseInt(n) + 1 }, this.getGdprConsentCode = function() {
          var n = r(e.getGdprStorageItemName()) || t(e.getGdprCookieName());
          if (null == n) return -1;
          var o = new Array;
          o = n.split(",");
          for (a in o) o[a] = parseInt(o[a], 10) + 1;
          return o.toString()
        }, this.getConsentDecision = function() {
          var e = this.getConsentCode();
          if (-1 == e) { var t = '{"consentDecision": 0, "source": "implied"}'; return JSON.parse(t) }
          t = '{"consentDecision": ' + parseInt(e) + ', "source": "asserted"}';
          return JSON.parse(t)
        }, this.getGdprConsentDecision = function() {
          var e = this.getGdprConsentCode();
          if (-1 == e) { var t = '{"consentDecision": [0], "source": "implied"}'; return JSON.parse(t) }
          t = '{"consentDecision": [' + e + '], "source": "asserted"}';
          return JSON.parse(t)
        }
      }.apply(oracle.truste.api);
  }

  //This is where we add infinity script dynamically to the page
  function addOracleInfinity() {
    trustechk["truste_consent"] = getTrusteConsentLevel();

    if (trustechk["truste_consent"] == -1 || trustechk["truste_consent"].indexOf(0) != -1) {
      //Check if Do Not Track settings is enabled in the browser.
      //navigator.msDoNotTrack - For Internet Explorer 9 & 10
      //window.doNotTrack - For Safari 7.1.3+, Internet Explorer 11 and Edge
      //navigator.doNotTrack - For Chrome and FF

      if (isDoNotTrackSet()) {
        trustechk["dnt"] = true;
      }
    }

    //Have to check here because DNT is not checked in all scenarios
    if (isDoNotTrackSet()) {
      log('Do Not Track is set.');
    } else {
      log('Do Not Track is NOT set.');
    }

    //Deploy the Infinity tag based on user's DoNotTrack and TrustArc consent preference.
    if ((trustechk['truste_consent'].indexOf(0) != -1 ||
        trustechk['truste_consent'].indexOf(2) != -1 ||
        trustechk['truste_consent'].indexOf(3) != -1) && (!trustechk['dnt'])) {
      //First deploy SiteCatalyst while we are still supporting it
      if (window.oraVersion == null) {
        addSiteCatalyst();
      }
      //Deploy the Infinity tag
      log("global.js Applying Oracle Infinity");
      var infiScript = document.createElement('script');
      infiScript.type = "text/javascript";
      if (window.location.hostname == "docs.oracle.com") {
        log("global.js Oracle Infinity: prod");
        infiScript.src = "https://c.oracleinfinity.io/acs/account/wh3g12c3gg/js/oracledocs/odc.js?_ora.context=analytics:production";
      } else {
        log("global.js Oracle Infinity: dev");
        infiScript.src = "https://c.oracleinfinity.io/acs/account/wh3g12c3gg/js/oracledocs/odc.js?_ora.context=analytics:development";
      }
      document.getElementsByTagName('head')[0].appendChild(infiScript);
    } else {
      log('Skipping Oracle Infinity');
    }
  }

  //Returns the TRUSTArc cookie level, which we use to discern if the Oracle Infinity tag should be added
  function getTrusteConsentLevel() {
    log('Checking TRUSTArc Settings...');
    var cStatus = new Array(-1, "NoTRUSTe"); //Set the default return variable...
    // Look up the TRUSTe setting using the oracle.truste.api.getConsentCode() fucntion...
    try {
      cStatus[0] = oracle.truste.api.getGdprConsentDecision().consentDecision;
      cStatus[1] = oracle.truste.api.getGdprConsentDecision().source;
    } catch (err) {
      cStatus[0] = -1;
    }
    //If the oracle.truste.api fails look up the TRUSTe setting using the truste.cma.callApi call.
    if (cStatus[0] == -1) {
      try {
        cStatus[0] = truste.cma.callApi("getGDPRConsentDecision", "oracle.com").consentDecision;
        cStatus[1] = truste.cma.callApi("getGDPRConsentDecision", "oracle.com").source;
      } catch (err) {
        cStatus[0] = -1;
      }
    }
    log('TRUSTArc setting: ' + cStatus[0] + ' ' + cStatus[1]);
    return cStatus[0]; //Return JUST the consent value...
  }

  function checkCustomDocArchDisclaimer(doc) {
    //NEWER direct from DocArch custom libraries: <a href="http://www.oracle.com/pls/topic/lookup?ctx=cpyr&amp;id=en-US" target="_blank">Legal Notices</a>
    var customDocArchDisclaimer = doc.querySelector("a[href*='//www.oracle.com/pls/topic/lookup?ctx=cpyr']");
    if (customDocArchDisclaimer) {
      log("customDocArchDisclaimer");
      //Now find Privacy Notice (only searching for this if legal element is found)
      var privacy = customDocArchDisclaimer.parentElement.querySelector("a[href*='//www.oracle.com/us/legal/privacy/index.html']");
      var privacy2 = doc.querySelector("a[href*='//www.oracle.com/us/legal/privacy/index.html']");
      if (privacy || privacy2) {
        if (!privacy && privacy2) {
          privacy = privacy2;
        }
        log("customDocArchDisclaimer>privacy");
        var spanSeparator1 = doc.createElement("span");
        spanSeparator1.innerHTML = " | ";

        var cookieLink = doc.createElement("a");
        cookieLink.id = "teconsent";
        cookieLink.href = "#";

        var spanSeparator2 = doc.createElement("span");
        spanSeparator2.innerHTML = " | ";

        var adLink = doc.createElement("a");
        adLink.id = "adchoices";
        adLink.target = "_blank";
        adLink.href = "https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12";
        adLink.innerHTML = 'Ad Choices';

        privacy.insertAdjacentElement('afterend', spanSeparator1);
        spanSeparator1.insertAdjacentElement('afterend', cookieLink);
        cookieLink.insertAdjacentElement('afterend', spanSeparator2);
        spanSeparator2.insertAdjacentElement('afterend', adLink);

        window.top.disclaimerSet = true;
        applyConsent(doc);
        return true;
      }
    }
    return false;
  }

  function tryFramedFooters() {
    //Always start from top window when doing this check because we may end up here from only a frame change
    var frames = window.top.frames;
    for (var i = 0; i < frames.length; i++) {
      try {
        /*************************
         * If any frame already shows a consent element, then bail because it has already
         * been handled at another level properly. Bail by returning true to let the
         * calling routine knows to avoid an hm strategy banner
         *************************/
        //Bail if teconsent element already exists
        if (frames[i].document.getElementById('teconsent') !== null) return true;

        if (checkCustomDocArchDisclaimer(frames[i].document) == true) {
          return true;
        }

        //Product / version string "Custom libraries direct from DocArch"
        //NEWER direct from DocArch custom libraries: <a href="http://www.oracle.com/pls/topic/lookup?ctx=cpyr&amp;id=en-US" target="_blank">Legal Notices</a>
        var prodVerDocArchDisclaimer = frames[i].document.querySelector("a[href*='217488.htm']");
        if (prodVerDocArchDisclaimer) {
          log("prodVerDocArchDisclaimer");

          var AdChoiceMsg = '<a id="adchoices" target="_blank" href="https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12">Ad Choices</a>';
          var disclaimerLi = prodVerDocArchDisclaimer.parentElement;
          var disclaimerUl = prodVerDocArchDisclaimer.parentElement.parentElement;

          //Consent
          var li1 = frames[i].document.createElement("li");
          if (disclaimerLi.className !== "") li1.className = disclaimerLi.className;
          li1.id = "teconsent";
          disclaimerUl.appendChild(li1);

          //Some pages intentionally turn off last border and we need it back on
          disclaimerLi.style.removeProperty('border-right-style');

          //Ad
          var li2 = frames[i].document.createElement("li");
          if (disclaimerLi.className !== "") li2.className = disclaimerLi.className;
          li2.innerHTML = AdChoiceMsg;
          disclaimerUl.appendChild(li2);

          //We need to turn off last right border, like they originally did
          li2.style = "border-right-style: none;";

          window.top.disclaimerSet = true;
          applyConsent(frames[i].document);
          return true;
        }
      } catch (error) {
        log("tryFramedFooters() Skipping frame in parent (" + frames[i].parent.location + ": i=" + i + ") because it might be CORS, or experienced other error: " + error);
      }
    } //frames loop
    return false; //No custom frame solution applied
  }

  //runs after dom and after a 2 second delay to allow other scripts to finish
  function delayedFunction() {
    //Only run on main frame
    if (isHttp() && (isNotFramed() || window.top.disclaimerSet == true)) {
      var consent = document.getElementById('teconsent');
      if (consent === null) {
        log("Trying known framed footers....");
        var framed = tryFramedFooters();

        //if (typeof window.top.disclaimerSet == 'undefined' || !window.top.disclaimerSet) {
        if (!framed) {
          log("Defaulting to fixed banner");
          addFooterBannerHMStrategy();
        }
      }

      //Note: siteCatalyst is added in addOracleInfinity function
      addOracleInfinity();
    } else {
      log('Skipping delayedFunction processing');
    }
  }

  //Runs after dom
  function onLoad() {
    log("global.js onLoad");
    log("window.top.location=" + window.top.location + " | window.location=" + window.location);
    var disclaimerSet = false;
	
	var pubProductName = window.self.document.querySelector('meta[name="dcterms.product"]');
	if (pubProductName && pubProductName.getAttribute("content") === "en/cloud/paas/digital-assistant") {
		var pubProductNameScript = document.createElement('script');
		pubProductNameScript.type = "text/javascript";
		pubProductNameScript.src = "/en/dcommon/artie/js/artie.js";
		document.getElementsByTagName('head')[0].appendChild(pubProductNameScript);
    }

    //See if existing teconsent element exists
    var consent = window.self.document.getElementById('teconsent');

    //For JavaDoc and PeopleSoft, we don't have any reported pre-existing
    //cookie prefs or ad choices so we treat it as all or nothing
    if (consent === null) {
      if (checkCustomDocArchDisclaimer(window.self.document) == true) {
        disclaimerSet = true;
        //Consent is already applied at lower level
      }

      var javaApiDisclaimer = window.self.document.querySelector("a[href*='//www.oracle.com/technetwork/java/redist-137594.html']");
      if (javaApiDisclaimer) {
        log("jd");
        var preCookieMsg = 'Modify ';
        var cookieMsg = '<a id="teconsent" href="#"></a>';
        var AdChoiceMsg = '. Modify <a id="adchoices" target="_blank" href="https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12">Ad Choices</a>.';
        var disclaimerElement = document.createElement('span');
        disclaimerElement.innerHTML = preCookieMsg + cookieMsg + AdChoiceMsg;
        javaApiDisclaimer.parentElement.appendChild(disclaimerElement);
        disclaimerSet = true;
        applyConsent();
      }

      var peopleSoftDisclaimer = window.self.document.querySelector("a.footerlink[href*='//www.oracle.com/us/legal/privacy/overview/index.html']");
      if (peopleSoftDisclaimer) {
        log("ps");
        var AdChoiceMsg = 'Ad Choices';
        var disclaimerDiv = peopleSoftDisclaimer.parentElement;
        var spanSeparator1 = document.createElement("span");
        var spanSeparator2 = document.createElement("span");
        spanSeparator1.className = "separator";
        spanSeparator1.id = "teconsent";
        spanSeparator2.className = "separator";
        var link = document.createElement("a");
        link.id = "adchoices";
        link.className = "footerlink";
        link.target = "_blank";
        link.href = "https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12";
        link.innerHTML = AdChoiceMsg;
        disclaimerDiv.appendChild(spanSeparator1);
        disclaimerDiv.appendChild(spanSeparator2);
        spanSeparator2.appendChild(link);
        disclaimerSet = true;
        applyConsent();
      }

      /* 
       * Other types here:
       */

      //OLDER direct from DocArch custom libraries: <a href="dcommon/html/cpyr.htm">Legal Notices</a>
      //Note: There are older docs with dcommon/cpyr.htm, but they can't work with this code and would have to have their own
      //      handling or hm strategy approach. This appears to have some font-size issues sometimes but works otherwise
      var oldCustomDocArchDisclaimer = window.self.document.querySelector("a[href*='dcommon/html/cpyr.htm']");
      var oldCustomDocArchDisclaimer2 = window.self.document.querySelector("#javasecopyright a[href*='legal/cpyr.htm']");
      if (oldCustomDocArchDisclaimer || oldCustomDocArchDisclaimer2) {
        if (!oldCustomDocArchDisclaimer && oldCustomDocArchDisclaimer2) {
          oldCustomDocArchDisclaimer = oldCustomDocArchDisclaimer2;
        }
        log("oldCustomDocArch");
        var cookieMsg = ' | <a id="teconsent" href="#"></a>';
        var AdChoiceMsg = ' | <a id="adchoices" target="_blank" href="https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12">Ad Choices</a>.';
        var disclaimerTd = oldCustomDocArchDisclaimer.parentElement;
        //Do extra font-size checks here
        var children = oldCustomDocArchDisclaimer.children;
        var font = null;
        if (children.length > 0) {
          for (var c = 0; c < children.length; c++) {
            if (children[c].tagName.toLowerCase() == "font") {
              var size = children[c].size;
              log('tag=' + children[c].tagName + " size=" + size);
              cookieMsg = '<font size="' + size + '">' + cookieMsg + '</font>';
              AdChoiceMsg = '<font size="' + size + '">' + AdChoiceMsg + '</font>';
            }
          }
        }
        var oldHtml = disclaimerTd.innerHTML;
        disclaimerTd.innerHTML = oldHtml + cookieMsg + AdChoiceMsg;
        disclaimerSet = true;
        applyConsent();
      }

      var zzLegalNoticeLink = window.self.document.querySelector("a.zz-legal-notice-link[href*='title.htm'");
      if (zzLegalNoticeLink) {
        log("zzLegalNotice");
        var AdChoiceMsg = 'Ad Choices';
        var linebreak = document.createElement("br");
        var separator = document.createTextNode(' | ');
        var disclaimerP = zzLegalNoticeLink.parentElement;
        var link1 = document.createElement("a");
        link1.id = "teconsent";
        link1.className = "zz-legal-notice-link";
        link1.href = "#";
        var link2 = document.createElement("a");
        link2.id = "adchoices";
        link2.className = "zz-legal-notice-link";
        link2.target = "_blank";
        link2.href = "https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12";
        link2.innerHTML = AdChoiceMsg;
        disclaimerP.appendChild(linebreak);
        disclaimerP.appendChild(link1);
        disclaimerP.appendChild(separator);
        disclaimerP.appendChild(link2);
        disclaimerSet = true;
        applyConsent();
      }

      //Special Primavera case for older pubs a[href*='46576.htm']
      var primaveraDisclaimer = window.self.document.querySelector("a[href*='46576.htm']");
      if (primaveraDisclaimer && !isToc()) {
        log("primavera");
        var disclaimerParent = primaveraDisclaimer.parentElement;
        var consentLink = document.createElement("a");
        var spanSeparator = document.createElement("span");
        var adLink = document.createElement("a");
        consentLink.id = "teconsent";
        consentLink.href = "#";
        spanSeparator.innerHTML = "&nbsp;";
        var AdChoiceMsg = 'Ad Choices';
        adLink.id = "adchoices";
        adLink.target = "_blank";
        adLink.href = "https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12";
        adLink.innerHTML = AdChoiceMsg;
        disclaimerParent.appendChild(consentLink);
        disclaimerParent.appendChild(spanSeparator);
        disclaimerParent.appendChild(adLink);
        disclaimerSet = true;
        applyConsent();
      }

      //Special health-sciences case for older pubs with no footer and HM is covering text
      //https://jira.oraclecorp.com/jira/browse/DPS-62785
      if (window.location.href.indexOf("health-sciences/cleartrial/CTRNS/index.html") > 0) {
        log("health-sciences 1");
        document.body.style.marginBottom = "50px";
      }
    
      //Special health-sciences case for older pubs in a frameset with no matching top window
      //https://jira.oraclecorp.com/jira/browse/DPS-62785
      //https://host/health-sciences/cleartrial/CTSVG/index_files/sheetXXX.htm
      if (window.location.href.indexOf("CTSVG/index_files/sheet") > 0) {
        var body = document.body;
        if (body && !isToc()) {
          log("health-sciences 2");
          var container = document.createElement("div");
          var list = document.createElement("ul");
          var consentList = document.createElement("li");
          var consentLink = document.createElement("a");
          var spanSeparator = document.createElement("span");
          var adList = document.createElement("li");
          var adLink = document.createElement("a");

          container.style = "text-align: center; margin 30 0 0 0;";
          list.style = "justify-content: center; display: inline-flex; list-style: none; padding: 0;";
          consentLink.id = "teconsent";
          consentLink.href = "#";
          spanSeparator.innerHTML = "&nbsp;|&nbsp;";
          var AdChoiceMsg = 'Ad Choices';
          adLink.id = "adchoices";
          adLink.target = "_blank";
          adLink.href = "https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12";
          adLink.innerHTML = AdChoiceMsg;

          body.appendChild(container);
          container.appendChild(list);
          list.appendChild(consentList);
          consentList.appendChild(consentLink);
          list.appendChild(adList);
          adList.appendChild(spanSeparator);
          adList.appendChild(adLink);
          disclaimerSet = true;
          applyConsent();
        }
      }
    } //end if consent == null


    //APLG is special so is in its own process handling logic here
    //Targets many aplg era pubs with the US "Your Privacy Rights" link
    var aplgDisclaimer = window.self.document.querySelector("ul > li > a[href*='//www.oracle.com/us/legal/privacy/index.html']");
    if (aplgDisclaimer) {
      log("aplg");
      var AdChoiceMsg = '<a id="adchoices" target="_blank" href="https://www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12">Ad Choices</a>';
      var disclaimerLi = aplgDisclaimer.parentElement;
      var disclaimerUl = aplgDisclaimer.parentElement.parentElement;

      //Consent element doesn't exist at all
      if (consent === null) {
        var li1 = window.self.document.createElement("li");
        if (disclaimerLi.className !== "") li1.className = disclaimerLi.className;
        li1.id = "teconsent";
        disclaimerUl.appendChild(li1);
        applyConsent();
      } else {
        //teconsent is present, if empty, apply consent where it stands
        if (consent.innerHTML === "") {
          applyConsent();
        }
      }

      //See if existing ad choices element exists
      var choices = window.self.document.querySelector('a[href*="//www.oracle.com/legal/privacy/marketing-cloud-data-cloud-privacy-policy.html#12"]');
      if (choices === null) {
        //Ad choices doesn't exist so add it
        var li2 = document.createElement("li");
        if (disclaimerLi.className !== "") li2.className = disclaimerLi.className;
        li2.innerHTML = AdChoiceMsg;
        disclaimerUl.appendChild(li2);
      }

      disclaimerSet = true;
    }

    //TODO: We may not need the if(!window.disclaimerSet) code below
    if (disclaimerSet) {
      window.top.disclaimerSet = true;
      log("Custom disclaimer set");
      //Note: siteCatalyst is added in addOracleInfinity function
      addOracleInfinity();
    } else if (window.location !== window.parent.location) {
      //Framed, we don't specifically address frames and fallback to hm strategy on top window
      //although, we will try certain special cases of framed windows but start from the top window
      //to find all frames to see if any match. The reason we don't normally address frames as they come in
      //while loading in global.js is because the top window usually has the disclaimers and we don't want doubles
      if (window.top.disclaimerSet) {
        log("delayedFunction for frames called");
        setTimeout(function() {
          //Handle left navigation invoked frame
          delayedFunction();
        }, 2000);
      } else {
        //We aren't adding a frame-based disclaimer here but we still apply the analytics
        //Note: siteCatalyst is added in addOracleInfinity function
        addOracleInfinity();
      }
    } else {
      //No disclaimer set so determine which hm strategy to do
      if (!window.top.disclaimerSet) {
        log("delayedFunction for main window called");
        setTimeout(function() {
          //hm strategy
          delayedFunction();
        }, 2000);
      }
    }
  }

  //Code below runs the onLoad method above after the dom is loaded
  if (document.readyState == 'complete') {
    onLoad();
  } else if (window.addEventListener) {
    window.addEventListener('load', onLoad, false);
  } else if (window.attachEvent) {
    window.attachEvent('onload', onLoad);
  } else {
    window.onload = onLoad;
  }

})();

function addReleaseNoticesInlineBanner() {
  const mainDivOhcGrid = document.querySelectorAll('body > header')[0];
  const thisUrlPathName = window.location.pathname;
  const PRODUCT_GROUP = "product-groups";
  const LATEST_DOCS = "latest-docs";
  const PREVIOUS_RELEASES = "previous-releases";
  const alreadyExistedComponent = document.getElementsByClassName('release-notices-css');
  if (mainDivOhcGrid && alreadyExistedComponent.length <= 0 && window.location.href.search("/en/") === -1 
    && window.location.href.search("/search/") === -1) {
    const jsonContentReleaseNoticesUrl = '/notices/release-notices.json';
    const bannerDiv = document.createElement('div');
    bannerDiv.className = 'release-notices-container';
    $(bannerDiv).load(jsonContentReleaseNoticesUrl, function(responseTxt, statusTxt, xhr) {
      if (statusTxt == "success") {
        const jsonParsedRelease = JSON.parse(responseTxt);
        let notFoundIndex = true;
        for (let index = 0; notFoundIndex && jsonParsedRelease && index < jsonParsedRelease[PRODUCT_GROUP].length; index++) {
          const previousReleasesSubName = jsonParsedRelease[PRODUCT_GROUP][index][PREVIOUS_RELEASES];
          for (let subIndex = 0; notFoundIndex && previousReleasesSubName && subIndex < previousReleasesSubName.length; subIndex++) {
            const nameInBannerDivPreviousReleases = new RegExp(jsonParsedRelease[PRODUCT_GROUP][index][PREVIOUS_RELEASES][subIndex], "g");
            if (nameInBannerDivPreviousReleases && thisUrlPathName.search(nameInBannerDivPreviousReleases) !== -1) {
              const link = document.createElement("link");
              const isSearchEnabled = jsonParsedRelease[PRODUCT_GROUP][index]["search-enabled"];
              const buttonText = getReleaseNoticesButtonText(isSearchEnabled, jsonParsedRelease[PRODUCT_GROUP][index]['button-text']);

              addReleaseNoticesStyle(bannerDiv, jsonParsedRelease[PRODUCT_GROUP][index], buttonText);
              if (document.getElementById('CONTENT')) {
                link.href = "/en/dcommon/css/release-notices-xs.css";
              } else {
                link.href = "/en/dcommon/css/release-notices.css";
              }
              link.rel = "stylesheet";
              link.type = "text/css";
              mainDivOhcGrid.parentNode.insertBefore(bannerDiv, mainDivOhcGrid);
              document.head.appendChild(link);
              notFoundIndex = false;
              setReleaseNoticesUrl(isSearchEnabled, jsonParsedRelease[PRODUCT_GROUP][index][LATEST_DOCS]);
            }
          }
        }
        if (notFoundIndex)
          console.log('Error while parsing release-notices.json')
      }
      if (statusTxt == "error")
        console.log("Error: " + xhr.status + ": " + xhr.statusText);
    });
  }
}

function setReleaseNoticesUrl(isSearchEnabled, latestDocs) {
  const buttonElement = document.getElementById("release-notices-btn");
  const metaNameDcTerms = document.querySelectorAll("meta[name='dcterms.isVersionOf']")[0];
  const nameInBannerDivLatestDocs = latestDocs;
  const contentNameRole = metaNameDcTerms && metaNameDcTerms.getAttribute("content") ? metaNameDcTerms.getAttribute("content").toLowerCase() : "*";
  const jsonUrl = nameInBannerDivLatestDocs[contentNameRole] ? nameInBannerDivLatestDocs[contentNameRole] : nameInBannerDivLatestDocs['*'];

  if (isSearchEnabled && contentNameRole !== "*") {
    const urlPath = window.location.pathname;
    const dcTermsTitleElement = document.querySelector("meta[name='dcterms.title']");
    const dcTermsTitle = dcTermsTitleElement ? dcTermsTitleElement.getAttribute("content") : "";
    const titleElement = document.querySelector("title");
    var searchTerm = '';

    if ((/(preface|index|toc|title|lot|lof|loe|glossary)\.html?$/.test(urlPath)) && dcTermsTitle) {
      searchTerm = dcTermsTitle;
    } else if (titleElement && titleElement.innerText.length > 0) {
      searchTerm = titleElement.innerText;
    } else {
      const heading = document.querySelector("h1,h2");
      searchTerm = heading ? heading.innerText || heading.textContent : "";
    }

    const xhr = new XMLHttpRequest()
    xhr.open("GET", jsonUrl);
    xhr.send();
    xhr.onload = function () {
      if (xhr.status === 200) {
        const parser = new DOMParser();
        const parsedHtml = parser.parseFromString(xhr.responseText, 'text/html');
        const productElement = parsedHtml.querySelector("meta[name='dcterms.product']");
        const bookElement = parsedHtml.querySelector("meta[name='dcterms.isVersionOf']");
        var product = productElement ? productElement.getAttribute("content").split(";")[0] : "";
        var book = bookElement ? bookElement.getAttribute("content") : "";

        if (product && book) {
          product = "&library=" + encodeURIComponent(product);
        } else if (product) {
          product = "&product=" + encodeURIComponent(product);
        }
        if (book) {
          book = "&book=" + encodeURIComponent(book);
        }

        buttonElement.addEventListener("click", function () {
          window.location = "/apps/ohcsearchclient/api/v1/search/luckypages/?q=" + encodeURIComponent(searchTerm) + product + book;
        });
      }
    };

    xhr.onerror = function () {
      buttonElement.addEventListener("click", function () {
        window.location = jsonUrl;
      });
    };
  } else {
    buttonElement.addEventListener("click", function () {
      window.location = jsonUrl;
    });
  }
}

function getReleaseNoticesButtonText(isSearchEnabled, jsonButtonText) {
  const bookMeta = document.querySelector("meta[name='dcterms.isVersionOf']") ?
    document.querySelector("meta[name='dcterms.isVersionOf']").getAttribute("content") : "";
  if (isSearchEnabled && bookMeta) {
    return "View latest";
  }
  return jsonButtonText ? jsonButtonText : "Go to Latest Release";
}

function addReleaseNoticesStyle(bannerDiv, jsonParsedRelease, buttonText) {
  const ua = window.navigator.userAgent;
  const msie = ua.indexOf("MSIE ");
  let isIESetting = false;
  if (msie > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./)) {
    isIESetting = true;
  }
  const divBannerImg = '<div><div class="img-warning"></div></div>';
  const divBannerText = '<div class="release-text">' + jsonParsedRelease['banner-text'] + '</div>';
  const divBannerButtonIntegration = '<button id="release-notices-btn" class="button-integration" ' +
    (isIESetting ? 'style="padding-top: 5px;"' : '') + '>' + buttonText + '</button>';
  const divBannerOnClickHide = ' onclick="for(let i=0; i<document.getElementsByClassName(\'release-notices-container\').length; i++) { ' +
    'document.getElementsByClassName(\'release-notices-container\').item(i).setAttribute(\'style\',\'display: none !important\'); }"';
  const divBannerButtonClose = '<button class="button-close" ' +
    (isIESetting ? 'style="padding-top: 5px;"' : '') +
    divBannerOnClickHide +
    '>Close this notice</button>';
  const divBannerSpanSpaceMax = '<span class="space-max"></span>';
  const divBannerSpanSpaceMin = '<span class="space-min"></span>';
  const divBannerSpanSpaceAux = '<span class="space-aux"></span>';
  const divBannerAuxDiv = divBannerSpanSpaceAux + '<div class="div-aux">' + divBannerImg + divBannerSpanSpaceMax +
    divBannerText + divBannerSpanSpaceMax + '</div>';
  const divBannerAuxDivButtons = '<div class="div-aux-buttons">' + divBannerButtonIntegration + divBannerSpanSpaceMin +
    divBannerButtonClose + '</div>' + divBannerSpanSpaceAux;
  const divBanner = '<div class="release-notices-css">' + divBannerAuxDiv + divBannerAuxDivButtons + '</div>'
  $(bannerDiv).html(divBanner);
}

document.addEventListener("DOMContentLoaded", function() {
  addReleaseNoticesInlineBanner();
});