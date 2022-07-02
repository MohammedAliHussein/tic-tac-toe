<script>
	import { Link } from "svelte-navigator";
    export let text = "";
	export let page = "/";
</script>

<div class="wrapper">
	<Link class="link" to={page}>
		{text}
	</Link>
</div>

<style>
    .wrapper > :global(a) {
		text-decoration: none;
		display: flex;
		justify-content: center;
		align-items: center;
    }
    
    :global(a) {
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

	:global(a):hover {
		background-color: white;
		color: black;
	} 
</style>