<%-- 
    Document   : index
    Created on : 22/06/2015, 09:29:20 PM
    Author     : Rita
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

    <!-- Basic Page Needs
================================================== -->
    <meta charset="utf-8">
    <title>BancoSA</title>
    <meta name="description" content="">
    <meta name="author" content=" Made by Keyners">
    <meta http-equiv="X-UA-Compatible" content="IE=9" />


    <!-- Mobile Specific Metas
================================================== -->
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

    <!-- PT Sans -->
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,300,600&subset=latin,latin-ext' rel='stylesheet' type='text/css'>

    <!-- Crete Roung -->
    <link href='http://fonts.googleapis.com/css?family=Crete+Round&subset=latin,latin-ext' rel='stylesheet' type='text/css'>

    <!-- CSS
================================================== -->
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="css/base.css">
    <link rel="stylesheet" href="css/skeleton.css">
    <link rel="stylesheet" href="css/layout.css">

    <!--[if lt IE 9]>
            <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>
    <script type="text/javascript" src="js/validate.js"></script>
    <script type="text/javascript" src="js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
    <link rel="stylesheet" type="text/css" href="js/fancybox/jquery.fancybox-1.3.4.css" media="screen" />
    <script type="text/javascript">
        $(document).ready(function () {
            $("a[rel=example_group]").fancybox({
                'transitionIn': 'none',
                'transitionOut': 'none',
                'titlePosition': 'over',
                'titleFormat': function (title, currentArray, currentIndex, currentOpts) {
                    return '<span id="fancybox-title-over">Image ' + (currentIndex + 1) + ' / ' + currentArray.length + (title.length ? ' &nbsp; ' + title : '') + '</span>';
                }
            });
        });
    </script>

</head>
<body>


    <header>			
        <nav>
            <div class='container'>
                <div class='five columns logo'>
                    <a href='#'>Software Avanzado</a>
                </div>

                <div class='eleven columns'>
                    <ul class='mainMenu'>
                        <li><a href='index.jsp' title='Home'>Home</a></li>
                        <li><a href='menuSeguro.jsp' title='Seguros'>Seguros</a></li>
                        <li><a href='#' title='Pricing'>Préstamos</a></li>
                        <li><a href='#' title='Blog'>Blog</a></li>
                        <li><a href='#' title='Portfolio'>Portfolio</a></li>
                        <li><a href='#' title='Contact'>Contact</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <div class='container'>
            <div class='slogan'>
                <div class='ten columns'>
                    <h1>Bancosa</h1>
                    <h2>In Bancosa we trust.</h2>
                </div>
            </div>
        </div>	
    </header>


    <section class='gray'>
        <div class='container'>
            <center>
                <h4>Iniciar Sesión</h4>
                <form name="frmLogin" action="Login" method="POST" >
                    <table width="100%">
                        <tr>
                            <td > <label >Usuario:</label></td>
                            <td> <input type="text" name="txtUsuario" id="txtCodigo" required="" text="Usuario"></td>
                        </tr>
                        <tr><td><br></td><td></td></tr>
                        <tr>
                            <td > <label>Contraseña:</label></td>
                            <td> <input type="password" name="txtPass" id="txtPass" required="" text="Contraseña"></td>
                        </tr>
                        <tr><td><br></td><td></td></tr>
                        <tr>
                            <td ></td>
                            <td><input  type="submit" value="Ingresar" name="btnIngresar" id="btnIngresar"></td>
                        </tr>
                    </table>
                </form>
                <center><% String res = (String) request.getAttribute("result");
                            if (res!=null){}else{res="";}%> 
                    <div><font color="red" ><%=res%> </font></div></center>
            </center>
            <center><a href='crearUsuario.jsp' class='button medium green'>Crear Usuario</a></center>

        </div>
    </section>


    <footer>
        <div class='container'>

            <div class='eight columns'>
                <h5>Proyecto SA</h5>
                <p>Mario Leonel Rodas  201114264</p>
                <p>Rita Mariela Guarán 201114613</p>
            </div>

            <div class='four columns social'>
                <h5>Social media</h5>
                <a href='#'><img src='images/social/dribbble.png'></a>
                <a href='#'><img src='images/social/facebook.png'></a>
                <a href='#'><img src='images/social/twitter.png'></a>
                <a href='#'><img src='images/social/forrst.png'></a>
            </div>

            <div class='four columns'>
                <h5>Get in touch</h5>
                <p>You can call us at:<br>
                    t: 161 - 123 - CALL - NOW</p>
                <p>Or drop us a line on:
                    e: <a href='mailto:contact@free.com'>contact@free.com</a></p>
            </div>


            <a id='top' href='#'>&uarr;</a>	
        </div>
    </footer>
    <script type="text/javascript">
        var form = $('form');

        $(document).ready(function () {
            form.validate({
                ignore: "",
                rules: {
                    'message': {
                        required: true,
                    },
                    'name': {
                        required: true,
                    },
                    'mail': {
                        required: true,
                        email: true
                    }
                },
                errorPlacement: function (error, element) {
                }

            });
        });
    </script>


    <script type="text/javascript">
        var toper = $('a#top');


        $(window).scroll(function () {
            if ($(this).scrollTop() > 100) {
                toper.fadeIn(200);
            } else {
                toper.fadeOut(200);
            }
        });

        toper.click(function () {
            $('html, body').animate({scrollTop: 0}, 500);
            return false;
        });
    </script>


</body>
</html>