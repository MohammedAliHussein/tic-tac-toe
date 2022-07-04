<script>
</script>

<div class="no-support">
    <h1>No Support</h1>
    <h5>Smaller screen sizes not supported.</h5>
</div>

<style>
    h1, h5 {
        color: white;
        animation-timing-function: cubic-bezier(0, 0.55, 0.45, 1);
        animation-duration: 1s;
    }

    h1 {
        animation-name: slideDown;
        margin: 0.25em;
    }

    h5 {
        animation-name: slideUp;
    }

    .no-support {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }
    
    @keyframes slideUp {
        0% {
            opacity: 0;
            transform: translateY(1.25em);
        }
        100% {
            opacity: 1;
            transform: translateY(0em);
        }
    }

    @keyframes slideDown {
        0% {
            opacity: 0;
            transform: translateY(-1em);
        }
        100% {
            opacity: 1;
            transform: translateY(0em);
        }
    }
</style>