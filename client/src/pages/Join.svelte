<script>
    import { fly } from "svelte/transition";
    import { circOut, circInOut } from "svelte/easing";
    import axios from "axios";

    let displayName = "";
    let gameId = "";
    let password = "";
    let requestError = false;
    let requestErrorMessage = "";

    function getIn(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: 20,
            easing: circOut
        }
    }

    function getOut(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: -20,
            easing: circInOut
        }
    }

    async function handleJoinGame() {
        const request = {
            display_name: displayName,
            game_id: gameId,
            password: password
        }

        const response = await axios.post("http://localhost:3001/join", {
            data: request,
            Headers: {
                "Access-Control-Allow-Origin": "*"
            }
        }).catch((error) => {
            requestErrorMessage = ((error.response).data).title;
            requestError = true;
            console.log(error.response);
        });
    }

    function clearError() {
        requestError = false;
    }
</script>

<div class="container">
    <div class="join">
        <h1 in:fly={getIn(0)}>Join a Game</h1>
        <div class="form">
            <h4 in:fly={getIn(1)}>Display Name</h4>
            <input type="text" placeholder="Display Name" in:fly={getIn(2)} bind:value={displayName} on:focus={clearError}>
            <h4 in:fly={getIn(3)} >Game ID</h4>
            <input type="text" placeholder="Game ID" in:fly={getIn(4)}  bind:value={gameId} on:focus={clearError}>
            <h4 in:fly={getIn(5)} >Game Password</h4>
            <input type="password" placeholder="Game Password" in:fly={getIn(6)} bind:value={password} on:focus={clearError}>
        </div>
        <button on:click|preventDefault={handleJoinGame} in:fly={getIn(7)}>Join Game</button>
    </div>
    <div class="request-error-container">
        {#if requestError}
            <div class="request-error" in:fly={getIn(0)} out:fly={getOut(0)}>
                <h5>{requestErrorMessage}</h5>
            </div>
        {/if}
    </div>
</div>

<style>
    .request-error-container {
        margin-top: 1em;
        height: 2em;
    }

    .request-error {
        margin-top: 1em;
        height: 2em;
    }

    .container {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
    }

    .join {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }

    .form {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        margin-top: 0.8em;
    }

    h1, h4, h5 {
        color: white;
        text-align: center;
        outline: none;
    }

    input {
        margin-bottom: 1em;
    }

    input {
        outline: none;
        border: 1px solid rgba(255, 255, 255, 0.091);
        text-align: center;
        margin-top: 10px;
        padding: 3px 13px;
        transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
        font-size: 12px;
        color: white;
    }

    input:focus {
        border: 1px solid rgba(255, 255, 255, 0.25);
    }

    button {
		outline: none;
		background: none;
		border: 1px solid white;
		cursor: pointer;
		padding: 2px 10px;
		margin-top: 15px;
		transition: 0.2s cubic-bezier(0, 0.55, 0.45, 1);
		font-size: 12px;
        margin-bottom: 5px;
		color: white;
    }

	button:hover {
		background-color: white;
		color: black;
	}     
</style>